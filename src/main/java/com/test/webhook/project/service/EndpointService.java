package com.test.webhook.project.service;

import com.test.webhook.project.payloads.EndpointDTO;

public interface EndpointService {
    
    EndpointDTO createEndpoint(EndpointDTO endpointDTO);
}
