package com.tickethandler.service;



import com.tickethandler.dto.TicketResolveResponse;
import com.tickethandler.dto.TicketResolverDto;
import com.tickethandler.dto.TicketResponse;

public interface TicketService {
	

	 public TicketResponse createTicket( int ticketTypeId, int productId, String ticketSummary, String ticketDetails, int ticketPriority);
	 public TicketResolveResponse resolveTicketAndAddLog(Long ticketNo, String logDetails);
	 public TicketResolverDto assignTicket(Long ticketNo);
		   
		    	   
}
