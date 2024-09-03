package com.tickethandler.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Requester;

//@Repository
public interface RequesterRepository extends JpaRepository<Requester, Integer>{

	//Boolean existsByrequesterEmail(String email);
	//Optional<Requester> findByrequesterEmail(String Email); 
}
