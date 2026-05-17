package kz.kbtu.notification.service.consumers;

import kz.kbtu.notification.service.controllers.NotificationController;
import kz.kbtu.notification.service.domain.Notification;
import kz.kbtu.notification.service.events.NotifyPreviousOwnerEvents;
import kz.kbtu.notification.service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tools.jackson.databind.ObjectMapper;

@Service
public class NotificationConsumer {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    NotificationRepository notificationRepository;

    @KafkaListener(topics = "notify-topic", groupId = "notification-group")
    public void consume(String message) {
        NotifyPreviousOwnerEvents event = objectMapper
                .readValue(message, NotifyPreviousOwnerEvents.class);

        System.out.println("Artifact \"" + event.artifactName()
                + "\" has new owner: " + event.newOwnerName());

        for (String email : event.previousOwnerEmails()) {

            String text = "⚠️ \"" + event.artifactName()
                    + "\" has a new keeper: " + event.newOwnerName()
                    + ". You have been warned.";


            Notification notification = Notification.builder()
                    .recipientEmail(email)
                    .message(text)
                    .build();
            notificationRepository.save(notification);
            System.out.println("Notification saved to DB for: " + email);


            SseEmitter emitter = NotificationController.emitters.get(email);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("curse-warning")
                            .data(text));
                    System.out.println("SSE push sent to: " + email);
                } catch (Exception e) {
                    System.out.println("SSE push failed for: " + email);
                    NotificationController.emitters.remove(email);
                }
            } else {
                System.out.println("Adam is offline — notification stored for later: " + email);
            }
        }
    }
}