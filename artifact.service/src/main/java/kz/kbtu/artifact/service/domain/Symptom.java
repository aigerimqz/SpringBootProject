package kz.kbtu.artifact.service.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "curse_symptom")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "artifact_id")
    private Artifact artifact;

    private String description;
    private String severity;
    private LocalDateTime loggedAt;


    @PrePersist
    public void prePersist() {
        this.loggedAt = LocalDateTime.now();
    }



}
