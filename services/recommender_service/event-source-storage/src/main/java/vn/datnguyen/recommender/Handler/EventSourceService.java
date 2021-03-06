package vn.datnguyen.recommender.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import vn.datnguyen.recommender.AvroClasses.AvroAddItem;
import vn.datnguyen.recommender.AvroClasses.AvroAddToCartBehavior;
import vn.datnguyen.recommender.AvroClasses.AvroBuyBehavior;
import vn.datnguyen.recommender.AvroClasses.AvroDeleteItem;
import vn.datnguyen.recommender.AvroClasses.AvroDeleteRating;
import vn.datnguyen.recommender.AvroClasses.AvroEvent;
import vn.datnguyen.recommender.AvroClasses.AvroPublishRating;
import vn.datnguyen.recommender.AvroClasses.AvroQueryRating;
import vn.datnguyen.recommender.AvroClasses.AvroUpdateRating;
import vn.datnguyen.recommender.Controller.EventConsumer;
import vn.datnguyen.recommender.Models.CachedEvent;
import vn.datnguyen.recommender.Models.EventEntity;
import vn.datnguyen.recommender.Models.OutboxEntity;
import vn.datnguyen.recommender.Repositories.CachedEventRepository;
import vn.datnguyen.recommender.Repositories.EventRepository;
import vn.datnguyen.recommender.Repositories.OutboxRepository;

@Service
public class EventSourceService implements EventHandler {

    @Value("${DBTable.clientIdCol}")
    private String clientIdCol;

    @Value("${DBTable.itemIdCol}")
    private String itemIdCol;

    @Value("${DBTable.scoreCol}")
    private String scoreCol;

    @Value("${DBTable.eventTypeCol}")
    private String eventTypeCol;

    @Value("${DBTable.property1Col}")
    private String property1Col;

    @Value("${DBTable.property2Col}")
    private String property2Col;

    @Value("${DBTable.property3Col}")
    private String property3Col;

    private Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private CachedEventRepository cachedEventRepository;
    private EventRepository eventRepository;
    private OutboxRepository outboxRepository;

    @Autowired
    public EventSourceService(CachedEventRepository cachedEventRepository,
                            EventRepository eventRepository, 
                            OutboxRepository outboxRepository) {
        this.cachedEventRepository = cachedEventRepository;
        this.eventRepository = eventRepository;
        this.outboxRepository = outboxRepository;
    }
    
    @Override
    public CompletableFuture<Void> process(AvroEvent event) {
        //logger.info("EVENT-SOURCE-STORAGE: consumer event " + event);
        return CompletableFuture.runAsync(
            () -> Stream.of(event).filter( ev -> {
                    try {
                        return isEventDuplicated(ev);
                    }
                    catch (Exception e) {
                        logger.warn("EVENT-SOURCE-STORAGE: filter exception" + e);
                    }
                    return false;
                }
            )
            .map(this::cachingEvent)
            .forEach(this::apply)
        );
    }

    private boolean isEventDuplicated(AvroEvent event) {
        String eventId = event.getEventId(); 
        boolean isEventCached = cachedEventRepository.existsById(eventId);
        return (!isEventCached);
    }

    private AvroEvent cachingEvent(AvroEvent event) {
        String eventId = event.getEventId(); 
        String eventType = event.getEventType();
        CachedEvent cacheEvent = new CachedEvent(eventId, eventType);
        cachedEventRepository.save(cacheEvent);
        return event;
    }
    
    @Override
    @Transactional
    public void apply(AvroEvent event) {
        logger.info("EVENT-SOURCE-STORAGE: consumer event after caching" + event);

        try {
            EventEntity eventEntity = new EventEntity();
            OutboxEntity outboxEntity = new OutboxEntity();

            eventEntity.setEventType(event.getEventType());
            
            Map<String, Object> payload = payloadFrom(event);

            eventEntity.setPayload(payload);
            eventEntity.serializePayload();

            Map<String, Object> outboxPayload = payload;
            outboxPayload.put(eventTypeCol, eventEntity.getEventType());

            outboxEntity.setPayloadJSON(outboxPayload);

            eventRepository.save(eventEntity);

            // delete data in outbox. So table won't grow. Because only need tailing log
            outboxRepository.save(outboxEntity);
            outboxRepository.delete(outboxEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> payloadFrom(AvroEvent event) {

        Object data = event.getData();

        if (data instanceof AvroPublishRating) {
            return payloadFrom((AvroPublishRating) data);
        }
        else if (data instanceof AvroUpdateRating) {
            return payloadFrom((AvroUpdateRating) data);
        }

        else if (data instanceof AvroDeleteRating) {
            return payloadFrom((AvroDeleteRating) data);
        }
        else if (data instanceof AvroQueryRating) {
            return payloadFrom((AvroQueryRating) data);
        }
        else if (data instanceof AvroBuyBehavior) {
            return payloadFrom((AvroBuyBehavior) data);
        }
        else if (data instanceof AvroAddToCartBehavior) {
            return payloadFrom((AvroAddToCartBehavior) data);
        }
        else if (data instanceof AvroAddItem) {
            return payloadFrom((AvroAddItem) data);
        }
        else if (data instanceof AvroDeleteItem) {
            return payloadFrom((AvroDeleteItem) data);
        }
        return null;
    }

    private Map<String, Object> payloadFrom(AvroPublishRating data) {
        Map<String,Object> payload = new HashMap<>();
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        payload.put(scoreCol, data.getScore());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroPublishRating: " + payload);
        return payload;
    }

    private Map<String, Object> payloadFrom(AvroUpdateRating data) {
        Map<String,Object> payload = new HashMap<>();
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        payload.put(scoreCol, data.getScore());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroUpdateRating: " + payload);
        return payload;
    }

    private Map<String, Object> payloadFrom(AvroDeleteRating data) {
        Map<String,Object> payload = new HashMap<>();
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroDeleteRating: " + payload);
        return payload;
    }

    private Map<String, Object> payloadFrom(AvroQueryRating data) {
        Map<String, Object> payload = new HashMap<>(); 
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroQueryRating: " + payload);
        return payload;
    }

    private Map<String, Object> payloadFrom(AvroBuyBehavior data) {
        Map<String, Object> payload = new HashMap<>(); 
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroBuyBehavior: " + payload);
        return payload;
    }

    private Map<String, Object> payloadFrom(AvroAddToCartBehavior data) {
        Map<String, Object> payload = new HashMap<>(); 
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroAddToCartBehavior: " + payload);
        return payload;
    }

    private Map<String, Object> payloadFrom(AvroAddItem data) {
        Map<String, Object> payload = new HashMap<>(); 
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        payload.put(property1Col, data.getProperties1());
        payload.put(property2Col, data.getProperties2());
        payload.put(property3Col, data.getProperties3());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroAddItem: " + payload);
        return payload;
    }

    private Map<String, Object> payloadFrom(AvroDeleteItem data) {
        Map<String, Object> payload = new HashMap<>(); 
        payload.put(clientIdCol, data.getClientId());
        payload.put(itemIdCol, data.getItemId());
        logger.info("EVENT-SOURCE-STORAGE: load data from AvroDeleteItem: " + payload);
        return payload;
    }

}
