package com.test.webhook.project.service;

import com.test.webhook.project.payloads.EndpointDTO;
import com.test.webhook.project.payloads.EndpointResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface EndpointService {
    
    EndpointDTO createEndpoint(EndpointDTO endpointDTO, HttpServletRequest request);

    EndpointResponse getAllEndpoints(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, HttpServletRequest request);
}
