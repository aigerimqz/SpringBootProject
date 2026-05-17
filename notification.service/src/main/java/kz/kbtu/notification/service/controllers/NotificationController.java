package kz.kbtu.notification.service.controllers;

import kz.kbtu.notification.service.domain.Notification;
import kz.kbtu.notification.service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    public static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Autowired
    NotificationRepository notificationRepository;


    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String email) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(email, emitter);
        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(()    -> emitters.remove(email));
        emitter.onError(e       -> emitters.remove(email));
        System.out.println("SSE subscription registered for: " + email);
        return emitter;
    }

    @GetMapping
    public List<Notification> getAll(@RequestParam String email) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    @GetMapping("/unread")
    public List<Notification> getUnread(@RequestParam String email) {
        return notificationRepository.findByRecipientEmailAndReadFalseOrderByCreatedAtDesc(email);
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable UUID id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    @PutMapping("/read-all")
    public void markAllAsRead(@RequestParam String email) {
        List<Notification> unread = notificationRepository
                .findByRecipientEmailAndReadFalseOrderByCreatedAtDesc(email);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }
}