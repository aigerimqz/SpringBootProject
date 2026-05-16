package kz.kbtu.auth.service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kbtu.auth.service.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisteredProducer {

    private static final String TOPIC = "user-registered-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public void publish(UserRegisteredEvent event) {
        String json = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(TOPIC, event.userId(), json);
        System.out.println("Published UserRegisteredEvent for: " + event.email());
    }
}