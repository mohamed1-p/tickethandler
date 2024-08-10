package com.tickethandler.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.ResolveTicketRequest;
import com.tickethandler.dto.TicketRequestDTO;
import com.tickethandler.dto.TicketResolverDto;
import com.tickethandler.dto.TicketResponse;
import com.tickethandler.model.Ticket;
import com.tickethandler.service.TicketServiceImpl;

@RestController
@RequestMapping("api/tickets")
public class TicketController {
    @Autowired
    private TicketServiceImpl ticketService;
    
    
    
    @GetMapping("/by-company")
    public ResponseEntity<List<Ticket>> getTicketsByCompanyName(@RequestParam String companyName) {
        List<Ticket> tickets = ticketService.getTicketsByCompanyName(companyName);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/by-product")
    public ResponseEntity<List<Ticket>> getTicketsByProductName(@RequestParam String productName) {
        List<Ticket> tickets = ticketService.getTicketsByProductName(productName);
        return ResponseEntity.ok(tickets);
    }    
    
    

    @PostMapping("/create")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequestDTO ticketRequest) {
    	TicketResponse createdTicket = ticketService.createTicket(
            ticketRequest.getRequesterId(),
            ticketRequest.getCompanyId(),
            ticketRequest.getTicketTypeId(),
            ticketRequest.getProductId(),
            ticketRequest.getTicketSummary(),
            ticketRequest.getTicketDetails(),
            ticketRequest.getTicketPriority()
        );

        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }
      
    
    @PutMapping("/{ticketNo}/assign")
    public ResponseEntity<TicketResolverDto> assignTicket(@PathVariable Long ticketNo, @RequestBody Map<String, Integer> request) {
        int engineerId = request.get("engineerId");
        TicketResolverDto assignedTicket = ticketService.assignTicket(ticketNo, engineerId);
        return ResponseEntity.ok(assignedTicket);
    }
    
    @PutMapping("/{ticketNo}/resolve")
    public ResponseEntity<Ticket> resolveTicketAndAddLog(@PathVariable Long ticketNo, @RequestBody ResolveTicketRequest request) {
        Ticket resolvedTicket = ticketService.resolveTicketAndAddLog(
            ticketNo,
            request.getEngineerId(),
            request.getLogDetails()
        );
        return ResponseEntity.ok(resolvedTicket);
    }
    

}