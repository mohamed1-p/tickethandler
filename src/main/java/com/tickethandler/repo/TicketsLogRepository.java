package com.tickethandler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.TicketsLog;

@Repository
public interface TicketsLogRepository extends JpaRepository<TicketsLog, Long> {

}


