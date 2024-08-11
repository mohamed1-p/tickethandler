package com.tickethandler.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tickethandler.model.ReqeusterRole;

public interface RequesterRoleRepository extends JpaRepository<ReqeusterRole, Integer>{

	Optional<ReqeusterRole> findByRole(String role);
}
