package com.tickethandler.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tickethandler.model.TicketStatus;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Integer>{

	TicketStatus findByStatusName(String string);

}
