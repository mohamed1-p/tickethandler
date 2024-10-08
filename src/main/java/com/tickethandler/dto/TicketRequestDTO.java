package com.tickethandler.dto;



import lombok.Data;

@Data
public class TicketRequestDTO {

    private int ticketTypeId;
    private int productId;
    private String ticketSummary;
    private String ticketDetails;
    private int ticketPriority;

    
}
