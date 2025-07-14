package com.test.webhook.project.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.webhook.project.model.Endpoint;
import com.test.webhook.project.payloads.EndpointDTO;
import com.test.webhook.project.repositories.EndpointRespository;

public class EndpointServiceImpl implements EndpointService{

    @Autowired
    private EndpointRespository endpointRespository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EndpointDTO createEndpoint(EndpointDTO endpointDTO) {
        Endpoint endpoint = modelMapper.map(endpointDTO, Endpoint.class);
        
       return null;
    }

}
