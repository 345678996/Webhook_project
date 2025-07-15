package com.test.webhook.project.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.webhook.project.exceptions.APIException;
import com.test.webhook.project.model.Endpoint;
import com.test.webhook.project.model.IncomingRequest;
import com.test.webhook.project.payloads.IncomingRequestDTO;
import com.test.webhook.project.repositories.EndpointRespository;
import com.test.webhook.project.repositories.IncomingRequestRespository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class IncomingRequestServiceImpl implements IncomingRequestService{

    @Autowired
    private IncomingRequestRespository incomingRequestRespository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EndpointRespository endpointRespository;

    @Override
    public IncomingRequestDTO handleIncomingRequest(String customEndpoint,
                                                    HttpServletRequest request,
                                                    String body, 
                                                    Map<String, String> headers) throws JsonProcessingException {
        
        Endpoint endpoint = endpointRespository.findByEndpointName(customEndpoint)
                    .orElseThrow(() -> new APIException("Please use a valid endpoint for sending the request"));
                    
        String method = request.getMethod();
        String headersJson = new ObjectMapper().writeValueAsString(headers);
        String queryParams = request.getQueryString();
        String path = request.getRequestURI();

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
        }

        IncomingRequest incomingRequest = IncomingRequest.builder()
                            .method(method)
                            .headers(headersJson)
                            .queryParams(queryParams)
                            .path(path)
                            .ipAddress(ipAddress)
                            .body(body)
                            .receivedAt(LocalDateTime.now())
                            .endpoint(endpoint)
                            .build();
        IncomingRequest savedRequest = incomingRequestRespository.save(incomingRequest);
        
        IncomingRequestDTO incomingRequestDTO = modelMapper.map(savedRequest, IncomingRequestDTO.class);
        incomingRequestDTO.setEndpointId(endpoint.getEndpointId());
        incomingRequestDTO.setEndpointName(customEndpoint);

        // Deserialize headers JSON back into Map<String, String>
        Map<String, String> headersMap = new ObjectMapper().readValue(
            savedRequest.getHeaders(),
            new com.fasterxml.jackson.core.type.TypeReference<>() {}
        );
        incomingRequestDTO.setHeaders(headersMap);
        
        return incomingRequestDTO;
    }

}
