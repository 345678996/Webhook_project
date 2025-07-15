package com.test.webhook.project.payloads;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomingRequestDTO {
    private Long id;
    private String method;
    private String headers;      
    private String body;         
    private String queryParams;  
    private String path;         
    private LocalDateTime receivedAt;
    private Long endpointId;     
    private String endpointName;
}
