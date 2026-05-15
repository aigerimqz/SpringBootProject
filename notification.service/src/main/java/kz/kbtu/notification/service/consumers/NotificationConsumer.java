package kz.kbtu.notification.service.consumers;

import kz.kbtu.notification.service.events.NotifyPreviousOwnerEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


@Service
public class NotificationConsumer {
    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = "notify-topic", groupId = "notification-group")
    public void consume(String message){
        NotifyPreviousOwnerEvents event = objectMapper.readValue(message, NotifyPreviousOwnerEvents.class);

        System.out.println("Artifact \"" + event.artifactName() + "\" has a new owner: " + event.newOwnerName());

        for(String email : event.previousOwnerEmails()){
            System.out.println("WARNING sent to previous owner: " + email);

        }
    }
}
