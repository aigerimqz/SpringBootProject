package kz.kbtu.notification.service.events;

import java.util.List;

public record NotifyPreviousOwnerEvents(
        String artifactId,
        String artifactName,
        String newOwnerName,
        List<String> previousOwnerEmails
) {
}
