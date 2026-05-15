package kz.kbtu.owner.service.client;


import kz.kbtu.owner.service.dto.ArtifactDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "artifact-service", url = "http://artifact-service:8081")
public interface ArtifactClient {
    @GetMapping("/api/artifacts/{id}")
    ArtifactDTO getArtifact(@PathVariable UUID id);

    @PostMapping("/api/artifacts/{id}/assign-owner")
    ArtifactDTO assignOwner(
            @PathVariable UUID id,
            @RequestParam String ownerName,
            @RequestParam String ownerEmail
            );
}


