package com.tickethandler.dto;

import lombok.Data;

@Data
public class TicketResolverDto {


	private Long ticketNo;
    private int requesterId;
    private String requesterName;
    private int companyId;
    private String companyName;
    private int ticketTypeId;
    private String ticketTypeName;
    private int productId;
    private String ticketSummary;
    private String ticketDetails;
    private int ticketPriority;
    private int EngineerId;
    private String EngineerName;
}
