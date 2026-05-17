package kz.kbtu.artifact.service.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kz.kbtu.artifact.service.domain.Artifact;
import kz.kbtu.artifact.service.domain.Symptom;
import kz.kbtu.artifact.service.service.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/artifacts")
public class ArtifactController {

    @Autowired
    private ArtifactService artifactService;


    @Operation(summary = "Register a new cursed artifact")
    @ApiResponse(responseCode = "200", description = "Artifact created")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Artifact create(@RequestBody Artifact artifact){
        return artifactService.create(artifact);
    }

    @Operation(summary = "Get all cursed artifacts")
    @ApiResponse(responseCode = "200", description = "List of all artifacts")
    @GetMapping
    public List<Artifact> getAll(){
        return artifactService.getAll();
    }


    @Operation(summary = "Get artifact by ID")
    @ApiResponse(responseCode = "200", description = "Artifact found")
    @ApiResponse(responseCode = "404", description = "Artifact not found")
    @GetMapping("/{id}")
    public Artifact getById(@PathVariable UUID id){
        return artifactService.getById(id);
    }



    @Operation(summary = "Assign new owner — triggers Kafka notification chain")
    @ApiResponse(responseCode = "200", description = "Owner assigned, previous owners notified")
    @PostMapping("/{id}/assign-owner")
    @PreAuthorize("hasRole('ADMIN')")
    public Artifact assignOwner(@PathVariable UUID id, @RequestParam String ownerName, @RequestParam String ownerEmail){
        return artifactService.assignOwner(id, ownerName, ownerEmail);
    }


    @Operation(summary = "Log a curse symptom for an artifact")
    @ApiResponse(responseCode = "200", description = "Symptom logged")
    @PostMapping("/{id}/symptoms")
    @PreAuthorize("hasRole('ADMIN')")
    public Symptom addSymptom(@PathVariable UUID id, @RequestBody Symptom symptom){
        return artifactService.addSymptom(id, symptom);
    }


    @Operation(summary = "Get all symptoms for an artifact")
    @ApiResponse(responseCode = "200", description = "List of symptoms")
    @GetMapping("/{id}/symptoms")
    public List<Symptom> getSymptoms(@PathVariable UUID id){
        return artifactService.getSymptoms(id);
    }


}
