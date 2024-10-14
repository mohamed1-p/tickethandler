package com.tickethandler.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Requester;
import com.tickethandler.model.SupportEngineer;
import com.tickethandler.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>,JpaSpecificationExecutor<UserEntity>{

	Optional<UserEntity> findByemail(String username);
	Boolean existsByemail(String username);
	Page<SupportEngineer> findByFullNameContaining(String name,Pageable pageable);
}
