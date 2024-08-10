package com.tickethandler.service;



import com.tickethandler.dto.TicketResolverDto;
import com.tickethandler.dto.TicketResponse;
import com.tickethandler.model.Ticket;

public interface TicketService {
	

	 public TicketResponse createTicket(int requesterId, int companyId, int ticketTypeId, int productId, String ticketSummary, String ticketDetails, int ticketPriority);
	 public Ticket resolveTicketAndAddLog(Long ticketNo, int engineerId, String logDetails);
	 public TicketResolverDto assignTicket(Long ticketNo, int engineerId);
		   
		    	   
}
