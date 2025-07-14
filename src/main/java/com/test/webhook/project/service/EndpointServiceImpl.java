package com.test.webhook.project.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.webhook.project.exceptions.APIException;
import com.test.webhook.project.model.Endpoint;
import com.test.webhook.project.payloads.EndpointDTO;
import com.test.webhook.project.repositories.EndpointRespository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class EndpointServiceImpl implements EndpointService{

    @Autowired
    private EndpointRespository endpointRespository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EndpointDTO createEndpoint(EndpointDTO endpointDTO, HttpServletRequest request) {
        Endpoint endpoint = modelMapper.map(endpointDTO, Endpoint.class);

        Endpoint endpointFromDB = endpointRespository.findByEndpointName(endpoint.getEndpointName());
        if(endpointFromDB != null) {
            throw new APIException("Endpoint with name "+endpointDTO.getEndpointName()+" already exist");
        }
        Endpoint savedEndpoint = endpointRespository.save(endpoint);

        EndpointDTO endpointDTOwithCustomUrl = modelMapper.map(savedEndpoint, EndpointDTO.class);
        
        String baseURL = request.getScheme() + "://" + request.getServerName()
               + ":" + request.getServerPort();
        endpointDTOwithCustomUrl.setCustomEndpointUrl(baseURL +"/api/"+ savedEndpoint.getEndpointName());
       
        return endpointDTOwithCustomUrl;
    }

    

}
