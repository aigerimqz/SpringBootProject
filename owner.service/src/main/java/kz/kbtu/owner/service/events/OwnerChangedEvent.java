package kz.kbtu.owner.service.events;

public record OwnerChangedEvent (
        String artifactId,
        String artifactName,
        String newOwnerName,
        String newOwnerEmail
){}
