package com.tickethandler.dto;

import lombok.Data;

@Data
public class TicketResolveResponse {

	private Long TicketId;
	private int ticketTypeId;
	private String ticketSummary;
    private String logDetails;
}
