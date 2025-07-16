package com.test.webhook.project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.webhook.project.configurations.AppConstants;
import com.test.webhook.project.payloads.IncomingRequestDTO;
import com.test.webhook.project.payloads.IncomingRequestResponse;
import com.test.webhook.project.service.IncomingRequestService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class IncomingRequestController {

    @Autowired
    private IncomingRequestService incomingRequestService;
    
    @RequestMapping(
        value = "/api/{customEndpoint}",
        method = {RequestMethod.GET,
                 RequestMethod.POST,
                 RequestMethod.PUT,
                 RequestMethod.DELETE,
                 RequestMethod.PATCH,
                 RequestMethod.OPTIONS,
                 RequestMethod.HEAD}
    )
    public ResponseEntity<IncomingRequestDTO> handleIncomingRequest(
        @PathVariable String customEndpoint,
        @RequestHeader Map<String, String> headers,
        HttpServletRequest request,
        @RequestBody(required = false) String body
    )   throws JsonProcessingException {
        
        IncomingRequestDTO requestDTO = incomingRequestService.handleIncomingRequest(customEndpoint, request, body, headers);
        return new ResponseEntity<>(requestDTO, HttpStatus.OK);

    }

    @GetMapping("/api/endpoints/{endpointName}/requests")
    public ResponseEntity<IncomingRequestResponse> getIncomingRequestsByEndpointName(
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_INCOMINGREQUEST_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
        HttpServletRequest request,
        @PathVariable String endpointName
    ) {
        IncomingRequestResponse response = incomingRequestService.getIncomingRequestsByEndpointName(
                                                pageNumber,
                                                pageSize,
                                                sortBy,
                                                sortOrder,
                                                request,
                                                endpointName
                                            );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/endpoints/{endpointName}/requests/{requestId}")
    public ResponseEntity<IncomingRequestDTO> getSingleRequestForEndpoint(
        HttpServletRequest request,
        @PathVariable String endpointName,
        @PathVariable Long requestId
    ) {
        IncomingRequestDTO responseDTO = incomingRequestService.getSingleRequestForEndpoint(
                                                request,
                                                endpointName,
                                                requestId
                                            );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
