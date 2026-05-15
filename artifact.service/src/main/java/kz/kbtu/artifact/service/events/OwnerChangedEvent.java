package kz.kbtu.artifact.service.events;

public record OwnerChangedEvent (
    String artifactId,
    String artifactName,
    String newOwnerName,
    String newOwnerEmail
){}
