package com.tickethandler.dto;

import lombok.Data;

@Data
public class TicketUpdateRequest {

	private String ticketSummary;
	private String ticketDetails;
}
