package com.test.webhook.project.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Endpoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endpointId;

    @Column(nullable = false, unique = true)
    private String endpointName;
    
    @Column(nullable = false)
    private String description;
    
    @PrePersist
    public void prePersistEndpoint() {
        if (endpointName == null) {
        endpointName = UUID.randomUUID().toString();
        }
        if (description == null) {
        description = "test endpoint";
        }
    }
    
    @CreationTimestamp
    private LocalDateTime createdAt;


}
