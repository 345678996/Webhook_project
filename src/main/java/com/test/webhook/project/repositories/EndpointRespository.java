package com.test.webhook.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.webhook.project.model.Endpoints;

public interface EndpointRespository extends JpaRepository<Endpoints, Long>{

}
