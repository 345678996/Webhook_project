package com.test.webhook.project.service;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.webhook.project.payloads.IncomingRequestDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface IncomingRequestService {

    IncomingRequestDTO handleIncomingRequest(String customEndpoint, HttpServletRequest request, String body, Map<String, String> headers) throws JsonProcessingException ;

}
