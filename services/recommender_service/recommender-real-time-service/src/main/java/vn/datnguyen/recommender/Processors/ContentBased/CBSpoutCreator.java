package vn.datnguyen.recommender.Processors.ContentBased;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.kafka.spout.ByTopicRecordTranslator;
import org.apache.storm.kafka.spout.FirstPollOffsetStrategy;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff;
import org.apache.storm.kafka.spout.KafkaSpoutRetryService;
import org.apache.storm.kafka.spout.KafkaSpoutConfig.ProcessingGuarantee;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff.TimeInterval;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import vn.datnguyen.recommender.Spout.CustomKafkaSpout;
import vn.datnguyen.recommender.utils.CustomProperties;

public class CBSpoutCreator {

    private final static CustomProperties customProperties = CustomProperties.getInstance();

    private final static String BOOTSTRAP_SERVER = customProperties.getProp("BOOTSTRAP_SERVER");
    private final static String EVENT_FOR_COMPUTING_TOPIC = customProperties.getProp("EVENT_FOR_COMPUTING_TOPIC");
    private final static String CB_METHOD_CONSUMER_GROUP = customProperties.getProp("CB_METHOD_CONSUMER_GROUP");
    private final static String EVENTSOURCE_STREAM_COMMAND = customProperties.getProp("EVENTSOURCE_STREAM_COMMAND");

    private final static String KAFKA_MESSAGE_HEADER_FIELD = customProperties.getProp("KAFKA_MESSAGE_HEADER_FIELD");
    private final static String TOPIC_FIELD = customProperties.getProp("TOPIC_FIELD");
    private final static String EVENT_FIELD = customProperties.getProp("EVENT_FIELD");
    private final static String KAFKA_KEY_FIELD = customProperties.getProp("KAFKA_KEY_FIELD");

    public CBSpoutCreator() {}

    public KafkaSpout<?,?> kafkaAvroEventSpout() {
        return new CustomKafkaSpout<>(kafkaAvroEventSpoutConfig());
    }
    
    private KafkaSpoutConfig<String, String> kafkaAvroEventSpoutConfig() {
        ByTopicRecordTranslator<String, String> byTopicTranslator = new ByTopicRecordTranslator<>(
            (r) -> new Values(r.headers(), r.topic(), r.key(), r.value()), 
            new Fields(KAFKA_MESSAGE_HEADER_FIELD, TOPIC_FIELD, KAFKA_KEY_FIELD, EVENT_FIELD),
            EVENTSOURCE_STREAM_COMMAND);
        
        //KafkaSpoutConfig.Builder<String, String> kafkaBuilder = new KafkaSpoutConfig.Builder<String, String>(BOOTSTRAP_SERVER, new String[]{LISTEN_FROM_TOPIC});

        return KafkaSpoutConfig.builder(BOOTSTRAP_SERVER, new String[]{EVENT_FOR_COMPUTING_TOPIC})
            .setProp(ConsumerConfig.GROUP_ID_CONFIG, CB_METHOD_CONSUMER_GROUP)
            .setProp(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
            // .setProp(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 300000)
            // .setProp(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000)
            // .setProp(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000)
            // .setProp(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100)
            // .setProp(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 600000)
            .setProcessingGuarantee(ProcessingGuarantee.AT_LEAST_ONCE)
            .setFirstPollOffsetStrategy(FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST)
            .setRetry(kafkaSpoutRetryService())
            .setRecordTranslator(byTopicTranslator)
            .build();
    }

    private KafkaSpoutRetryService kafkaSpoutRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(TimeInterval.microSeconds(500), 
            TimeInterval.milliSeconds(2), 
            Integer.MAX_VALUE, 
            TimeInterval.seconds(20));
    }
}
