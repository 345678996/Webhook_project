package com.test.webhook.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.webhook.project.model.IncomingRequest;

public interface IncomingRequestRespository extends JpaRepository<IncomingRequest, Long>{

}
