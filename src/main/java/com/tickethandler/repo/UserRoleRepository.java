package com.tickethandler.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

	Optional<UserRole> findByRole(String role);
}
