package com.test.webhook.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.webhook.project.exceptions.APIException;
import com.test.webhook.project.exceptions.ResourceNotFoundException;
import com.test.webhook.project.model.Endpoint;
import com.test.webhook.project.model.IncomingRequest;
import com.test.webhook.project.payloads.IncomingRequestDTO;
import com.test.webhook.project.payloads.IncomingRequestResponse;
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

    @Override
    public IncomingRequestResponse getIncomingRequestsByEndpointName(Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder, HttpServletRequest request, String endpointName) {
        
        Endpoint endpoint = endpointRespository.findByEndpointName(endpointName)
                    .orElseThrow(() -> new ResourceNotFoundException("Endpoint", "endpointName", endpointName));

        // ----Sorting---
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending(); 
        // ----Pagenation formula----
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<IncomingRequest> productPage = incomingRequestRespository.findByEndpointOrderByReceivedAtAsc(endpoint, pageDetails);

        List<IncomingRequest> incomingRequests = productPage.getContent();
        if(incomingRequests.isEmpty()) {
            throw new APIException("No requests at the specific endpoint");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        List<IncomingRequestDTO> incomingRequestDTOs = incomingRequests.stream()
                .map(req -> {
                    IncomingRequestDTO dto = modelMapper.map(req, IncomingRequestDTO.class);
                    dto.setEndpointId(req.getEndpoint().getEndpointId());
                    dto.setEndpointName(req.getEndpoint().getEndpointName());
                    try {
                        Map<String, String> headersMap = objectMapper.readValue(
                            req.getHeaders(),
                            new com.fasterxml.jackson.core.type.TypeReference<>() {}
                        );
                        dto.setHeaders(headersMap);
                    } catch (JsonProcessingException e) {
                        dto.setHeaders(null);
                    }
                    return dto;
                })
                .toList();

        IncomingRequestResponse incomingRequestResponse = new IncomingRequestResponse();
        incomingRequestResponse.setContent(incomingRequestDTOs);
        incomingRequestResponse.setPageNumber(productPage.getNumber());
        incomingRequestResponse.setPageSize(productPage.getSize());
        incomingRequestResponse.setTotalElements(productPage.getTotalElements());
        incomingRequestResponse.setTotalPages(productPage.getTotalPages());
        incomingRequestResponse.setLastPage(productPage.isLast());
        
        return incomingRequestResponse;
    }

}
