package vn.datnguyen.recommender.Processors.ContentBased;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.datnguyen.recommender.AvroClasses.Item;
import vn.datnguyen.recommender.AvroClasses.RecommendItemSimilaritesResult;
import vn.datnguyen.recommender.Models.KnnResult;
import vn.datnguyen.recommender.Models.RecommendedItem;
import vn.datnguyen.recommender.utils.CustomProperties;

public class RingAggregationBolt extends BaseRichBolt {
    
    private final Logger logger = LoggerFactory.getLogger(RingAggregationBolt.class);
    private final static CustomProperties customProperties = CustomProperties.getInstance();
    //VALUE FIELDS
    private final static String EVENT_COORD_FIELD = customProperties.getProp("EVENT_COORD_FIELD");
    private final static String KNN_FACTOR_FIELD = customProperties.getProp("KNN_FACTOR_FIELD");
    private final static String RING_LIST_FIELD = customProperties.getProp("RING_LIST_FIELD");
    private final static String CENTRE_LIST_FIELD = customProperties.getProp("CENTRE_LIST_FIELD");
    private final static String ITEM_ID_LIST_FIELD = customProperties.getProp("ITEM_ID_LIST_FIELD");
    private final static String DIST_LIST_FIELD = customProperties.getProp("DIST_LIST_FIELD");
    private final static String CENTRE_ID_FIELD = customProperties.getProp("CENTRE_ID_FIELD");
    private final static String RING_ID_FIELD = customProperties.getProp("RING_ID_FIELD");
    private final static String EVENT_ID_FIELD = customProperties.getProp("EVENT_ID_FIELD");
    private final static String KAFKA_MESSAGE_HEADER_FIELD = customProperties.getProp("KAFKA_MESSAGE_HEADER_FIELD");
    private final static String KAFKA_VALUE_FIELD = customProperties.getProp("KAFKA_VALUE_FIELD");
    //stream 
    private final static String AGGREGATE_BOUNDED_RINGS_STREAM = customProperties.getProp("AGGREGATE_BOUNDED_RINGS_STREAM");
    private final static String INDIVIDUAL_KNN_ALGORITHM_STREAM = customProperties.getProp("INDIVIDUAL_KNN_ALGORITHM_STREAM");
    //
    private OutputCollector collector;
    private Map<String, Integer> knnFactor;
    private Map<String, Set<ImmutablePair<Integer, UUID> > > potentialRingsForDelayedEvent;
    private Map<String, Set<ImmutablePair<Integer, UUID> > > potentialRingsForEvent;
    // store all "came-before-actual-event" result <dist,itemId>
    private Map<String, List<ImmutablePair<Double, String> > > bnnResultForDelayEvent;
    // PQ store K closest <dist, itemId>
    private Map<String, PriorityQueue<ImmutablePair<Double, String> > > mapKNNPQ;
    private Map<String, RecordHeaders> mapHeaders;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext TopologyContext, OutputCollector collector) {
        this.collector = collector;
        potentialRingsForEvent = new HashMap<>();
        potentialRingsForDelayedEvent = new HashMap<>();
        mapKNNPQ = new HashMap<>();
        bnnResultForDelayEvent = new HashMap<>();
        knnFactor = new HashMap<>();
        mapHeaders = new HashMap<>();
    }

    private void initCachedMap(String eventId, int centreId, UUID ringId, int K, RecordHeaders messageHeaders) {

        //init message header for event
        if (mapHeaders.containsKey(eventId)
            && !mapHeaders.get(eventId).equals(messageHeaders)) {
            logger.warn("********* RingAggregationBolt **********: Same event collided message header");
        }
        if (!mapHeaders.containsKey(eventId)) {
            mapHeaders.put(eventId, messageHeaders);
        }
        //init knn factor
        if (!knnFactor.containsKey(eventId)) {
            knnFactor.put(eventId, K);
        }

        Comparator<ImmutablePair<Double, String> > customCompartor = (p1, p2) -> {
            double dist1 = p1.getLeft();
            double dist2 = p2.getLeft();

            int cmp = (dist1 < dist2) ? 1
                        : -1;
            return cmp;
        };

        // init pq
        PriorityQueue<ImmutablePair<Double, String> > knnPQ;
        if (mapKNNPQ.containsKey(eventId)) {
            knnPQ = mapKNNPQ.get(eventId);
        } else {
            knnPQ = new PriorityQueue<>(customCompartor);
        }

        // add set to potential rings
        Set<ImmutablePair<Integer, UUID> > ringIdentitySet;

        if (potentialRingsForEvent.containsKey(eventId)) {
            ringIdentitySet = potentialRingsForEvent.get(eventId);
        } else {
            ringIdentitySet = new HashSet<>();
        }
        ringIdentitySet.add(new ImmutablePair<Integer,UUID>(centreId, ringId));

        // if have any rings came BEFORE event come
        if (potentialRingsForDelayedEvent.containsKey(eventId)) {
            logger.info("********* RingAggregationBolt **********: Have some rings came before event");
            Set<ImmutablePair<Integer, UUID> > ringSet = potentialRingsForDelayedEvent.get(eventId);
            ringIdentitySet.removeAll(ringSet);
            //erase all results have came
            potentialRingsForDelayedEvent.remove(eventId);

            //maintain pq
            List<ImmutablePair<Double, String> > resultList = bnnResultForDelayEvent.get(eventId);
            for (ImmutablePair<Double, String> result : resultList) {

                knnPQ.add(result); 

                if (knnPQ.size() > K) {
                    knnPQ.poll();
                }
            }

            bnnResultForDelayEvent.remove(eventId);
        }

        logger.info("********* RingAggregationBolt **********: Init waited ring and priority queue");
        potentialRingsForEvent.put(eventId, ringIdentitySet);
        mapKNNPQ.put(eventId, knnPQ);
    }

    private void processResultFromRingHandler(String eventId, int centreId, UUID ringId, String itemId, double itemDist) {
        // ring identity 
        ImmutablePair<Integer, UUID> ringIdentity = new ImmutablePair<Integer,UUID>(centreId, ringId);
        //list of result 
        List<ImmutablePair<Double, String> > resultListForDelayEvent;

        if (bnnResultForDelayEvent.containsKey(eventId)) {
            resultListForDelayEvent = bnnResultForDelayEvent.get(eventId);
        } else {
            resultListForDelayEvent = new ArrayList<>();
        }

        // if event haven't came yet or ring haven't come
        if (!potentialRingsForEvent.containsKey(eventId)
            || !potentialRingsForEvent.get(eventId).contains(ringIdentity)) {
            // add result to cached for later
            resultListForDelayEvent.add(new ImmutablePair<Double,String>(itemDist, itemId));

            logger.info("********* RingAggregationBolt **********: No event has came");
            if (!(potentialRingsForDelayedEvent.containsKey(eventId))) {
                Set<ImmutablePair<Integer, UUID> > ringSet = new HashSet<>();
                ringSet.add(ringIdentity);
                potentialRingsForDelayedEvent.put(eventId, ringSet);

                bnnResultForDelayEvent.put(eventId, resultListForDelayEvent);
            } else {
                Set<ImmutablePair<Integer, UUID> > ringSet = 
                    potentialRingsForDelayedEvent.get(eventId); 
                ringSet.add(ringIdentity);
                potentialRingsForDelayedEvent.replace(eventId, ringSet);

                bnnResultForDelayEvent.replace(eventId, resultListForDelayEvent);
            }
        }
        else {
            logger.info("********* RingAggregationBolt **********: Event has came - " + eventId);
            Set<ImmutablePair<Integer, UUID> > currRingSetForEventId = 
                potentialRingsForEvent.get(eventId);

            if (currRingSetForEventId.size() == 0) {
                logger.warn("********* RingAggregationBolt **********: currRingSetForEventId size must be > 0 ");
            }

            if (!currRingSetForEventId.contains(ringIdentity)) {
                logger.warn("********* RingAggregationBolt **********: currRingSetForEventId must contain ring - " + ringIdentity);
            }

            //remove form curr ring set 
            currRingSetForEventId.remove(ringIdentity);
            potentialRingsForEvent.replace(eventId, currRingSetForEventId);
            //maintain pq
            int K = knnFactor.get(eventId);
            PriorityQueue<ImmutablePair<Double, String> > currPQ = mapKNNPQ.get(eventId);
            
            currPQ.add(new ImmutablePair<Double,String>(itemDist, itemId)); 

            if (currPQ.size() > K) {
                currPQ.poll();
            }

            mapKNNPQ.replace(eventId, currPQ);

        }
    }

    private void purgeAllCached(String eventId) {
        knnFactor.remove(eventId);
        potentialRingsForDelayedEvent.remove(eventId);
        potentialRingsForEvent.remove(eventId);
        bnnResultForDelayEvent.remove(eventId);
        mapKNNPQ.remove(eventId);
        mapHeaders.remove(eventId);
    }

    private KnnResult getKNNResult(String eventId) {
        KnnResult knnResult = new KnnResult(eventId);

        PriorityQueue<ImmutablePair<Double, String> > currPQ = 
            mapKNNPQ.get(eventId);
        
        Deque<ImmutablePair<Double, String> > queue = new LinkedList<>();
        while (currPQ.size() > 0) {
            queue.addFirst(currPQ.poll());
        }

        while (queue.size() > 0) {
            knnResult.addToRecommendationList(queue.poll());
        }

        logger.info("********* RingAggregationBolt **********: RESULT = "
                    + knnResult.toString());

        purgeAllCached(eventId);

        return knnResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Tuple input) {

        String tupleSource = input.getSourceStreamId();
        RecordHeaders headers = null;
        KnnResult result = null;

        if (tupleSource.equals(AGGREGATE_BOUNDED_RINGS_STREAM)) {

            List<Integer> eventCoord = (List<Integer>) input.getValueByField(EVENT_COORD_FIELD);
            String eventId = (String) input.getValueByField(EVENT_ID_FIELD);
            int K = (int) input.getValueByField(KNN_FACTOR_FIELD);
            int centreId = (int) input.getValueByField(CENTRE_LIST_FIELD);
            UUID ringId = UUID.fromString( (String) input.getValueByField(RING_LIST_FIELD) );
            RecordHeaders messageHeaders = (RecordHeaders) input.getValueByField(KAFKA_MESSAGE_HEADER_FIELD);

            logger.info("********* RingAggregationBolt **********: FROM AGGREGATE_BOUNDED_RINGS_STREAM" 
                        + " eventId = " + eventId
                        + " eventCoord = " + eventCoord
                        + " KNN factor = " + K
                        + " centre id = " + centreId
                        + " ring id = " + ringId
                        + " kafka message header = " + messageHeaders);
            
            // init values 
            initCachedMap(eventId, centreId, ringId, K, messageHeaders);

            // some how all needed value came first ^_^ 
            if (potentialRingsForEvent.containsKey(eventId) && potentialRingsForEvent.get(eventId).size() == 0) {
                //do something
                headers = mapHeaders.get(eventId);
                result = getKNNResult(eventId);
            }
        } 
        else if (tupleSource.equals(INDIVIDUAL_KNN_ALGORITHM_STREAM)) {
            List<Integer> eventCoord = (List<Integer>) input.getValueByField(EVENT_COORD_FIELD);
            String eventId = (String) input.getValueByField(EVENT_ID_FIELD);
            int centreId = (int) input.getValueByField(CENTRE_ID_FIELD);
            UUID ringId = UUID.fromString((String) input.getValueByField(RING_ID_FIELD));
            String itemId = (String) input.getValueByField(ITEM_ID_LIST_FIELD);
            double itemDist = (double) input.getValueByField(DIST_LIST_FIELD);

            logger.info("********* RingAggregationBolt **********: FROM INDIVIDUAL_KNN_ALGORITHM_STREAM" 
                        + " eventId = " + eventId
                        + " eventCoord = " + eventCoord
                        + " centreId = " + centreId
                        + " ringId = " + ringId 
                        + " itemId = " + itemId
                        + " dist = " + itemDist);
            processResultFromRingHandler(eventId, centreId, ringId, itemId, itemDist);

            if (potentialRingsForEvent.containsKey(eventId) && potentialRingsForEvent.get(eventId).size() == 0) {
                headers = mapHeaders.get(eventId);
                result = getKNNResult(eventId);
            }
        }

        if (result != null) {

            List<Item> similaritesItem = new ArrayList<>();
            for (RecommendedItem item : result.getRecommendedItemList()) {
                Item avroItem = Item.newBuilder()
                    .setItemId(item.getItemId())
                    .setDifference(item.getDistance())
                    .build();
                
                similaritesItem.add(avroItem);
            }

            RecommendItemSimilaritesResult avroResult = RecommendItemSimilaritesResult.newBuilder()
                .setId(result.getEventId())
                .setTimestamp(Long.toString(System.currentTimeMillis()))
                .setSimilarities(similaritesItem)
                .build();

            collector.emit(input, new Values(headers, avroResult));
        }

        collector.ack(input);
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(KAFKA_MESSAGE_HEADER_FIELD, KAFKA_VALUE_FIELD));
    }
}
