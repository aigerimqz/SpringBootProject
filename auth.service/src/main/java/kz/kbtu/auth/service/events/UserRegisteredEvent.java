package kz.kbtu.auth.service.events;

public record UserRegisteredEvent (
        String userId,
        String email,
        String name
) {}
