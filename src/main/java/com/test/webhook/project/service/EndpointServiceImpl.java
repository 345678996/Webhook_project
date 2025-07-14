package com.test.webhook.project.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.test.webhook.project.exceptions.APIException;
import com.test.webhook.project.exceptions.ResourceNotFoundException;
import com.test.webhook.project.model.Endpoint;
import com.test.webhook.project.payloads.EndpointDTO;
import com.test.webhook.project.payloads.EndpointResponse;
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

        Optional<Endpoint> endpointFromDB = endpointRespository.findByEndpointName(endpoint.getEndpointName());
        if(endpointFromDB.isPresent()) {
            throw new APIException("Endpoint with name "+endpointDTO.getEndpointName()+" already exist");
        }
        Endpoint savedEndpoint = endpointRespository.save(endpoint);

        EndpointDTO endpointDTOwithCustomUrl = modelMapper.map(savedEndpoint, EndpointDTO.class);
        
        String baseURL = request.getScheme() + "://" + request.getServerName()
               + ":" + request.getServerPort();
        endpointDTOwithCustomUrl.setCustomEndpointUrl(baseURL +"/api/"+ savedEndpoint.getEndpointName());
       
        return endpointDTOwithCustomUrl;
    }

    @Override
    public EndpointResponse getAllEndpoints(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, HttpServletRequest request) {
        // ---Sorting---
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending(); 
        // ----Pagenation formula----
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Endpoint> endpointPage = endpointRespository.findAll(pageDetails);

        List<Endpoint> endpoints = endpointPage.getContent();
        if(endpoints.isEmpty()) {
            throw new APIException("No endpoints created till now");
        }
        List<EndpointDTO> endpointDTOs = endpoints.stream()
                        .map(ep -> modelMapper.map(ep, EndpointDTO.class))
                        .toList();
        String baseURL = request.getScheme() + "://" + request.getServerName()
               + ":" + request.getServerPort();

        endpointDTOs.forEach(endpointdto -> endpointdto.setCustomEndpointUrl(baseURL + "/api/" + endpointdto.getEndpointName()));


        EndpointResponse endpointResponse = new EndpointResponse();
        endpointResponse.setContent(endpointDTOs);
        endpointResponse.setPageNumber(endpointPage.getNumber());
        endpointResponse.setPageSize(endpointPage.getSize());
        endpointResponse.setTotalElements(endpointPage.getTotalElements());
        endpointResponse.setTotalPages(endpointPage.getTotalPages());
        endpointResponse.setLastPage(endpointPage.isLast());
        return endpointResponse;
    }

    @Override
    public EndpointDTO searchEndpointById(Long endpointId, HttpServletRequest request) {
        Endpoint endpointFromDB = endpointRespository.findById(endpointId)
                    .orElseThrow(() -> new ResourceNotFoundException("Endpoint","endpointId", endpointId));

        EndpointDTO endpointDTOFromDB = modelMapper.map(endpointFromDB, EndpointDTO.class);
        String baseURL = request.getScheme() + "://" + request.getServerName()
               + ":" + request.getServerPort();

        endpointDTOFromDB.setCustomEndpointUrl(baseURL +"/api/"+ endpointDTOFromDB.getEndpointName());
        
        return endpointDTOFromDB;
    }

    @Override
    public EndpointDTO searchEndpointByName(String endpointName, HttpServletRequest request) {
        Endpoint endpointFromDB = endpointRespository.findByEndpointName(endpointName)
                    .orElseThrow(() -> new ResourceNotFoundException("Endpoint","endpointName", endpointName));
        
                    EndpointDTO endpointDTOFromDB = modelMapper.map(endpointFromDB, EndpointDTO.class);
        String baseURL = request.getScheme() + "://" + request.getServerName()
               + ":" + request.getServerPort();

        endpointDTOFromDB.setCustomEndpointUrl(baseURL +"/api/"+ endpointDTOFromDB.getEndpointName());
        
        return endpointDTOFromDB;
    }


    

}
