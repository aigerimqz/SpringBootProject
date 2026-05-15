package kz.kbtu.artifact.service.producers;

import kz.kbtu.artifact.service.events.OwnerChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


@Service
public class OwnerChangedProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void sendOwnerChanged(OwnerChangedEvent event){
        String eventAsJson = objectMapper.writeValueAsString(event);

        kafkaTemplate.send("owner-changed-topic", eventAsJson);
        System.out.println("[Notification] Sent owner changed event: " + eventAsJson);
    }
}
