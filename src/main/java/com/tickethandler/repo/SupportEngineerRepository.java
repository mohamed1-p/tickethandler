package com.tickethandler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.SupportEngineer;

@Repository
public interface SupportEngineerRepository extends JpaRepository<SupportEngineer, Integer> {

}
