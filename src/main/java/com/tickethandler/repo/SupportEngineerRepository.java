package com.tickethandler.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.SupportEngineer;

//@Repository
public interface SupportEngineerRepository extends JpaRepository<SupportEngineer, Integer> {

	//Boolean existsByengineerEmail(String email);
	//Optional<SupportEngineer> findByengineerEmail(String Email);
}
