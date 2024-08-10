package com.tickethandler.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long>{

	public List<Ticket> findByCompany_CompanyName(String companyName);
	public List<Ticket> findByProduct_ProductName(String productName);
	
}
