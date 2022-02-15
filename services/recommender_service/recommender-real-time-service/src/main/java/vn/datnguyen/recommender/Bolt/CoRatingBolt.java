package vn.datnguyen.recommender.Bolt;

import java.util.List;
import java.util.Map;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.datnguyen.recommender.CassandraConnector;
import vn.datnguyen.recommender.Models.Event;
import vn.datnguyen.recommender.Repository.CoRatingRepository;
import vn.datnguyen.recommender.Repository.KeyspaceRepository;
import vn.datnguyen.recommender.Repository.RepositoryFactory;
import vn.datnguyen.recommender.utils.CustomProperties;

/**
 * coratingUPQ = min(Rup, Ruq)

    exist P . then update all (P-Q)-U and (Q-P)-U. 
    R_new_up < Ruq -> = R_new_up
    R_new_up > Ruq: => Ruq

    ---------------------------
    not exist P 
    co-rating(P,P) = R_new_up

    get all Q . then insert all (P-Q)-U and (Q-P)-U
    R_new_up < Ruq -> R_new_up
 */

public class CoRatingBolt extends BaseRichBolt {
    
    private final Logger logger = LoggerFactory.getLogger(CoRatingBolt.class);
    private final static CustomProperties customProperties = CustomProperties.getInstance();
    private OutputCollector collector;
    //TABLE COLUMN KEY
    private static final String ITEM_1_ID = "item_1_id";
    private static final String ITEM_2_ID = "item_2_id";
    private static final String RATING_ITEM_1 = "rating_item_1";
    private static final String RATING_ITEM_2 = "rating_item_2";
    //VALUE FIELDS
    private final static String OLD_RATING = customProperties.getProp("OLD_RATING");
    private final static String EVENT_FIELD = customProperties.getProp("EVENT_FIELD");
    private final static String KEYSPACE_FIELD = customProperties.getProp("KEYSPACE_FIELD");
    private final static String NUM_NODE_REPLICAS_FIELD = customProperties.getProp("NUM_NODE_REPLICAS_FIELD");
    //CASSANDRA PROPS
    private final static String CASS_NODE = customProperties.getProp("CASS_NODE");
    private final static String CASS_PORT = customProperties.getProp("CASS_PORT");
    private final static String CASS_DATA_CENTER = customProperties.getProp("CASS_DATA_CENTER");

    private RepositoryFactory repositoryFactory;
    private CoRatingRepository coRatingRepository;

    private void launchCassandraKeyspace() {
        CassandraConnector connector = new CassandraConnector();
        connector.connect(CASS_NODE, Integer.parseInt(CASS_PORT), CASS_DATA_CENTER);
        CqlSession session = connector.getSession();

        this.repositoryFactory = new RepositoryFactory(session);
        KeyspaceRepository keyspaceRepository = this.repositoryFactory.getKeyspaceRepository();
        keyspaceRepository.createAndUseKeyspace(KEYSPACE_FIELD, Integer.parseInt(NUM_NODE_REPLICAS_FIELD));
        logger.info("CREATE AND USE KEYSPACE SUCCESSFULLY keyspace in **** CoRatingBolt ****");
    }

    public void createTableIfNotExists() {
        SimpleStatement createTableStatement = this.coRatingRepository.createRowIfNotExists();
        this.repositoryFactory.executeStatement(createTableStatement, KEYSPACE_FIELD);
        logger.info("*** CoRatingBolt ****: " + "row created ");

        SimpleStatement createIndexStatement = this.coRatingRepository.createIndexOnItemId();
        this.repositoryFactory.executeStatement(createIndexStatement, KEYSPACE_FIELD);
        logger.info("*** CoRatingBolt ****: " + "index item id created ");

        createIndexStatement = this.coRatingRepository.createIndexOnClientId();
        this.repositoryFactory.executeStatement(createIndexStatement, KEYSPACE_FIELD);
        logger.info("*** CoRatingBolt ****: " + "index client id created ");
    }

    
    @Override
    public void prepare(Map<String, Object> map, TopologyContext TopologyContext, OutputCollector collector) {
        this.collector = collector;

        launchCassandraKeyspace();
        this.coRatingRepository = repositoryFactory.getCoRatingRepository();
        createTableIfNotExists();
    }
    
    @Override
    public void execute(Tuple input) {
        Event incomeEvent = (Event) input.getValueByField(EVENT_FIELD);
        int oldRating = (int) input.getValueByField(OLD_RATING);
        int newRating = incomeEvent.getWeight();
        String itemId = incomeEvent.getItemId();
        String clientId = incomeEvent.getClientId();

        SimpleStatement findByItem1Statement = this.coRatingRepository.findByItem1IdAndClientId(itemId, clientId);
        ResultSet findByItem1Result = this.repositoryFactory.executeStatement(findByItem1Statement, KEYSPACE_FIELD);

        List<Row> findByItem1 = findByItem1Result.all();
        int rowFound =findByItem1.size();

        if (rowFound == 0) {
            SimpleStatement findSetClientIdStatement = this.coRatingRepository.findSetItemIdByClientId(clientId);
            ResultSet findSetClientIdResult = this.repositoryFactory.executeStatement(findSetClientIdStatement, KEYSPACE_FIELD);
            List<Row> findSetClientId = findSetClientIdResult.all();

            executeWhenItemNotFound(findSetClientId, clientId, itemId, oldRating, newRating);
        } else {
            SimpleStatement findByItem2Statement = this.coRatingRepository.findByItem2IdAndClientId(itemId, clientId);
            ResultSet findByItem2Result = this.repositoryFactory.executeStatement(findByItem2Statement, KEYSPACE_FIELD);
            List<Row> findByItem2 = findByItem2Result.all();

            executeWhenItemFound(findByItem1, findByItem2, clientId, itemId, oldRating, newRating);
        }

        logger.info("********* CoRatingBolt **********" + incomeEvent + " with old rating = " + oldRating);
        Values values = new Values(incomeEvent);
        collector.emit(values);
        collector.ack(input);
    }


    private void executeWhenItemNotFound(List<Row> allItem1Id, String clientId, String itemId, int oldRating, int newRating) {
        BatchStatementBuilder executeWhenItemNotFound = BatchStatement.builder(BatchType.LOGGED);

        SimpleStatement insertNewItemId = this.coRatingRepository.insertNewItemScore(itemId, itemId, clientId);
        SimpleStatement updateItemIdScoreStatement = this.coRatingRepository.updateItemScore(itemId, itemId, clientId, newRating, newRating);
        SimpleStatement updateItem1IdRatingStatement = this.coRatingRepository.updateItem1Rating(itemId, itemId, clientId, newRating);
        SimpleStatement updateItem2IdRatingStatement = this.coRatingRepository.updateItem2Rating(itemId, itemId, clientId, newRating);

        executeWhenItemNotFound.addStatement(insertNewItemId)
            .addStatement(updateItemIdScoreStatement)
            .addStatement(updateItem1IdRatingStatement)
            .addStatement(updateItem2IdRatingStatement);

        for (Row r : allItem1Id) {
            String otherItemId = (String) this.repositoryFactory.getFromRow(r, ITEM_1_ID);
            int otherItemRating = (int) this.repositoryFactory.getFromRow(r, RATING_ITEM_1);

            logger.info("********* CoRatingBolt **********: Found itemId = " + otherItemId + " with rating = " + otherItemRating);

            int newScore = Math.min(newRating, otherItemRating);
            insertNewItemId = this.coRatingRepository.insertNewItemScore(itemId, otherItemId, clientId);
            updateItemIdScoreStatement = this.coRatingRepository.updateItemScore(itemId, otherItemId, clientId, newScore, newScore);
            updateItem1IdRatingStatement = this.coRatingRepository.updateItem1Rating(itemId, otherItemId, clientId, newRating);
            updateItem2IdRatingStatement = this.coRatingRepository.updateItem2Rating(itemId, otherItemId, clientId, otherItemRating);

            executeWhenItemNotFound.addStatement(insertNewItemId)
                .addStatement(updateItemIdScoreStatement)
                .addStatement(updateItem1IdRatingStatement)
                .addStatement(updateItem2IdRatingStatement);
            // 
            insertNewItemId = this.coRatingRepository.insertNewItemScore(otherItemId, itemId, clientId);
            updateItemIdScoreStatement = this.coRatingRepository.updateItemScore(otherItemId, itemId, clientId, newScore, newScore);
            updateItem1IdRatingStatement = this.coRatingRepository.updateItem1Rating(otherItemId, itemId, clientId, otherItemRating);
            updateItem2IdRatingStatement = this.coRatingRepository.updateItem2Rating(otherItemId, itemId, clientId, newRating);

            executeWhenItemNotFound.addStatement(insertNewItemId)
                .addStatement(updateItemIdScoreStatement)
                .addStatement(updateItem1IdRatingStatement)
                .addStatement(updateItem2IdRatingStatement);
        }

        BatchStatement allBatch = executeWhenItemNotFound.build();
        logger.info("********* CoRatingBolt **********: Attempt to execute " + allBatch.size() + " queries in batch");
        this.repositoryFactory.executeStatement(allBatch, KEYSPACE_FIELD);
        logger.info("********* CoRatingBolt **********: executed when item rating NOT found");
    }

    private void executeWhenItemFound(List<Row> findByItem1, List<Row> findByItem2, String clientId, String itemId, int oldRating, int newRating) {

        BatchStatementBuilder executeWhenItemFoundBatch = BatchStatement.builder(BatchType.LOGGED);

        for (Row r: findByItem1) {
            String item2Id = (String) this.repositoryFactory.getFromRow(r, ITEM_2_ID);
            int item2Rating = (int) this.repositoryFactory.getFromRow(r, RATING_ITEM_2);

            logger.info("********* CoRatingBolt **********: Found item 2 = " + item2Id + " with rating = " + item2Rating);

            SimpleStatement updateScoreItem;

            if (newRating < item2Rating) {
                int deltaScore = newRating - oldRating;
                updateScoreItem = this.coRatingRepository.updateItemScore(itemId, item2Id, clientId, newRating, deltaScore);
            } else {
                int deltaScore = item2Rating - Math.min(oldRating, item2Rating);
                updateScoreItem = this.coRatingRepository.updateItemScore(itemId, item2Id, clientId, item2Rating, deltaScore);
            }

            SimpleStatement updateRatingItem = this.coRatingRepository.updateItem1Rating(itemId, item2Id, clientId, newRating);

            executeWhenItemFoundBatch.addStatement(updateScoreItem)
                .addStatement(updateRatingItem);
        }

        for (Row r: findByItem2) {
            String item1Id = (String) this.repositoryFactory.getFromRow(r, ITEM_1_ID);
            int item1Rating = (int) this.repositoryFactory.getFromRow(r, RATING_ITEM_1);

            logger.info("********* CoRatingBolt **********: Found item 1 = " + item1Id + " with rating = " + item1Rating);

            SimpleStatement updateScoreItem;

            if (newRating < item1Rating) {
                int deltaScore = newRating - oldRating;
                updateScoreItem = this.coRatingRepository.updateItemScore(item1Id, itemId, clientId, newRating, deltaScore);
            } else {
                int deltaScore = item1Rating - Math.min(oldRating, item1Rating);
                updateScoreItem = this.coRatingRepository.updateItemScore(item1Id, itemId, clientId, item1Rating, deltaScore);
            }

            SimpleStatement updateRatingItem = this.coRatingRepository.updateItem2Rating(item1Id, itemId, clientId, newRating);

            executeWhenItemFoundBatch.addStatement(updateScoreItem)
                .addStatement(updateRatingItem);
        }

        BatchStatement allBatch = executeWhenItemFoundBatch.build();
        logger.info("********* CoRatingBolt **********: Attempt to execute " + allBatch.size() + " queries in batch");
        this.repositoryFactory.executeStatement(allBatch, KEYSPACE_FIELD);
        logger.info("********* CoRatingBolt **********: executed when item rating found");

    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("EVENT_FIELD"));
    }
}