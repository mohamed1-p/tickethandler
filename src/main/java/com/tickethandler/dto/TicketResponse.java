package com.tickethandler.dto;

import lombok.Data;

@Data
public class TicketResponse {

	private Long TicketId;
	private int ticketTypeId;
	private int ticketPriority;
	private String ticketSummary;
    private String ticketDetails;
    
}
