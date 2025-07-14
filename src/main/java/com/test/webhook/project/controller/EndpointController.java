package com.test.webhook.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.webhook.project.payloads.EndpointDTO;
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
}
