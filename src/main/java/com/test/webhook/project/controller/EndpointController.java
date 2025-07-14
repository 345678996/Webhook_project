package com.test.webhook.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.webhook.project.configurations.AppConstants;
import com.test.webhook.project.payloads.EndpointDTO;
import com.test.webhook.project.payloads.EndpointResponse;
import com.test.webhook.project.service.EndpointService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class EndpointController {

    @Autowired
    private EndpointService endpointService;

    @PostMapping("/api/endpoints")
    public ResponseEntity<EndpointDTO> createEndpoint(@RequestBody EndpointDTO endpointDTO,
                                                       HttpServletRequest request) {
        EndpointDTO savedEndpointDTO = endpointService.createEndpoint(endpointDTO, request);
        return new ResponseEntity<>(savedEndpointDTO, HttpStatus.CREATED);
    }

    @GetMapping("/api/endpoints")
    public ResponseEntity<EndpointResponse> getAllEndpoints(
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ENDPOINT_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
        HttpServletRequest request
    ) {
        EndpointResponse endpointResponse = endpointService.getAllEndpoints(pageNumber,pageSize,sortBy,sortOrder,request);

        return new ResponseEntity<>(endpointResponse, HttpStatus.OK);
    }

    @GetMapping("/api/endpoints/{endpointId}")
    public ResponseEntity<EndpointDTO> searchEndpointById(@PathVariable Long endpointId, HttpServletRequest request) {
        EndpointDTO endpointDTO = endpointService.searchEndpointById(endpointId,request);
        return new ResponseEntity<>(endpointDTO, HttpStatus.OK);
    }


}
