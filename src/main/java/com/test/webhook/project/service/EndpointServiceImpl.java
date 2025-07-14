package com.test.webhook.project.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.webhook.project.payloads.EndpointDTO;
import com.test.webhook.project.repositories.EndpointRespository;

public class EndpointServiceImpl implements EndpointService{

    @Autowired
    private EndpointRespository endpointRespository;

    @Override
    public EndpointDTO createEndpoint(EndpointDTO endpointDTO) {

       return null;
    }

}
