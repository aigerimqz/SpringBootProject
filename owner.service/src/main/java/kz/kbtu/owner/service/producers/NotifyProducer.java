package kz.kbtu.owner.service.producers;

import kz.kbtu.owner.service.events.NotifyPreviousOwnersEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


@Service
public class NotifyProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void sendNotifyEvent(NotifyPreviousOwnersEvent event){
        String eventAsJson = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("notify-topic", eventAsJson);
        System.out.println("Notify event sent to topic: " + eventAsJson);
    }
}
