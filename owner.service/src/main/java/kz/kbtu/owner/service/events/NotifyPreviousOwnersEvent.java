package kz.kbtu.owner.service.events;

import java.util.List;

public record NotifyPreviousOwnersEvent(
        String artifactId,
        String artifactName,
        String newOwnerName,
        List<String> previousOwnerEmails
) {
}
