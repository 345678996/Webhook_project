package com.test.webhook.project.service;

import com.test.webhook.project.payloads.EndpointDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface EndpointService {
    
    EndpointDTO createEndpoint(EndpointDTO endpointDTO, HttpServletRequest request);
}
