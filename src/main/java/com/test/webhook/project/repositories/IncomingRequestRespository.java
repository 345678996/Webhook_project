package com.test.webhook.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.webhook.project.model.Endpoint;
import com.test.webhook.project.model.IncomingRequest;

@Repository
public interface IncomingRequestRespository extends JpaRepository<IncomingRequest, Long>{

    Page<IncomingRequest> findByEndpointOrderByReceivedAtAsc(Endpoint endpoint, Pageable pageDetails);

    Optional<IncomingRequest> findByRequestIdAndEndpoint(Long requestId, Endpoint endpoint);

    List<IncomingRequest> findByEndpointOrderByReceivedAtAsc(Endpoint endpoint);

}
