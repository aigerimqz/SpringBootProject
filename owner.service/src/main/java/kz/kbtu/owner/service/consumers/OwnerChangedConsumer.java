package kz.kbtu.owner.service.consumers;

import kz.kbtu.owner.service.domain.Ownership;
import kz.kbtu.owner.service.events.NotifyPreviousOwnersEvent;
import kz.kbtu.owner.service.events.OwnerChangedEvent;
import kz.kbtu.owner.service.producers.NotifyProducer;
import kz.kbtu.owner.service.repository.OwnerRepository;
import kz.kbtu.owner.service.repository.OwnershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class OwnerChangedConsumer {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OwnershipRepository ownershipRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    NotifyProducer notifyProducer;

    @KafkaListener(topics = "owner-changed-topic", groupId = "cursed-group")
    public void consume(String message){
        OwnerChangedEvent event = objectMapper.readValue(message, OwnerChangedEvent.class);

        System.out.println("New owner: " + event.newOwnerName() + " for artifact: " + event.artifactName());

        List<Ownership> history = ownershipRepository.findByArtifactId(UUID.fromString(event.artifactId()));

        List<String> previousEmails = history.stream().map(o -> o.getOwner().getEmail()).distinct().toList();

        ownerRepository.findByEmail(event.newOwnerEmail()).ifPresent(owner -> {
            Ownership ownership = Ownership.builder()
                    .artifactId(UUID.fromString(event.artifactId()))
                    .owner(owner)
                    .acquiredAt(LocalDateTime.now())
                    .build();
            ownershipRepository.save(ownership);
            System.out.println("Saved ownership: " + owner.getName());
        });

        if (!previousEmails.isEmpty()) {
            NotifyPreviousOwnersEvent notifyEvent = new NotifyPreviousOwnersEvent(
                    event.artifactId(),
                    event.artifactName(),
                    event.newOwnerName(),
                    previousEmails
            );
            notifyProducer.sendNotifyEvent(notifyEvent);
        }
    }
}
