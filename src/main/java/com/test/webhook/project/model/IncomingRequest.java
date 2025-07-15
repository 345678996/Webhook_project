package com.test.webhook.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IncomingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;
    
    private String method;

    @Lob
    private String headers;
    @Lob
    private String body;
    @Lob
    private String queryParams;
    @Lob
    private String path;
    
    private LocalDateTime receivedAt;

    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "endpoint_id")
    private Endpoint endpoint;

}
