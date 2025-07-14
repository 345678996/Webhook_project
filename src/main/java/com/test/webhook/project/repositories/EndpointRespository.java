package com.test.webhook.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.webhook.project.model.Endpoint;

public interface EndpointRespository extends JpaRepository<Endpoint, Long>{

}
