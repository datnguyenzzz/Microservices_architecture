package vn.datnguyen.recommender.MessageQueue;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import vn.datnguyen.recommender.Serialization.AvroEvent;

@Configuration
public class FactoryConsumerConfiguration {

    @Value("${transactionKafka.bootstrapServers}")
    private String bootstrapServer;

    @Value("${groupId}")
    private String groupId;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AvroEvent> 
        kafkaListenerContainerFactory(ConsumerFactory<String, AvroEvent> consumerFactory) {
            ConcurrentKafkaListenerContainerFactory<String, AvroEvent> factory = 
                new ConcurrentKafkaListenerContainerFactory<String, AvroEvent>();
            
            factory.setConsumerFactory(consumerFactory);
            return null;
    }

    @Bean
    public ConsumerFactory<String, AvroEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

}