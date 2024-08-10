package com.tickethandler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Requester;

@Repository
public interface RequesterRepository extends JpaRepository<Requester, Integer>{

}
