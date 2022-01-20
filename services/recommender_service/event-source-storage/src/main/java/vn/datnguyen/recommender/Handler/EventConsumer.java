package vn.datnguyen.recommender.Handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import vn.datnguyen.recommender.AvroClasses.AvroEvent;
import vn.datnguyen.recommender.MessageQueue.Consumer;

@Component
public class EventConsumer implements Consumer {

    private EventSourceService eventSourceService;

    @Autowired
    public EventConsumer(EventSourceService eventSourceService) {
        this.eventSourceService = eventSourceService;
    }

    @KafkaListener(topics = "${ConsumerKafka.topicConsumerFromRatingCommand}", id = "${ConsumerKafka.groupId}")
    @Override
    public void execute(AvroEvent event) {
        this.eventSourceService.process(event);
    }
}
