package com.tickethandler.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{

	Optional<UserEntity> findByemail(String username);
	Boolean existsByemail(String username);
}
