package com.tickethandler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Requester;
import com.tickethandler.model.TicketType;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Integer>{


}
