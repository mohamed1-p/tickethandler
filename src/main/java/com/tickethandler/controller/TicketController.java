package com.tickethandler.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.tickethandler.dto.TicketResolveResponse;
import com.tickethandler.dto.TicketResolverDto;
import com.tickethandler.dto.TicketResponse;
import com.tickethandler.dto.TicketUpdateRequest;
import com.tickethandler.model.Ticket;
import com.tickethandler.service.TicketServiceImpl;

@RestController
@RequestMapping("api/tickets")
public class TicketController {
	
    @Autowired
    private TicketServiceImpl ticketService;
    
    
    //changed 
    @GetMapping("/by-company")
    public ResponseEntity<List<TicketResponse>> getTicketsByCompany() {
        List<TicketResponse> tickets = ticketService.getTicketsByCompany();
        return ResponseEntity.ok(tickets);
    }

    
    @GetMapping("/by-product")
    public ResponseEntity<List<TicketResponse>> getTicketsByProductName(@RequestParam String productName) {
        List<TicketResponse> tickets = ticketService.getTicketsByProductName(productName);
        return ResponseEntity.ok(tickets);
    }    
    
//    @GetMapping("/list")
//    public ResponseEntity<List<TicketResponse>> listTickets(
//           
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) Long productId,
//            @RequestParam(required = false) Boolean currentUserOnly
//    ) {
//        
//        List<TicketResponse> tickets = ticketService.listTickets(status, productId, currentUserOnly);
//        return ResponseEntity.ok(tickets);
//    }
    
    @PutMapping("/{ticketNo}/update")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long ticketNo,
            @RequestBody TicketUpdateRequest updateRequest) {
    	
        TicketResponse updatedTicket = ticketService.updateTicket(ticketNo, updateRequest);
        return ResponseEntity.ok(updatedTicket);
    }

    @PostMapping("/create")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequestDTO ticketRequest) {
    	TicketResponse createdTicket = ticketService.createTicket(
            ticketRequest.getTicketTypeId(),
            ticketRequest.getProductId(),
            ticketRequest.getTicketSummary(),
            ticketRequest.getTicketDetails(),
            ticketRequest.getTicketPriority()
        );

        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }
      
    
    @PutMapping("/{ticketNo}/assign")
    public ResponseEntity<TicketResolverDto> assignTicket(@PathVariable Long ticketNo) {
        TicketResolverDto assignedTicket = ticketService.assignTicket(ticketNo);
        return ResponseEntity.ok(assignedTicket);
    }
    
    @PutMapping("/{ticketNo}/resolve")
    public ResponseEntity<TicketResolveResponse> resolveTicketAndAddLog(@PathVariable Long ticketNo, @RequestBody ResolveTicketRequest request) {
    	TicketResolveResponse resolvedTicket = ticketService.resolveTicketAndAddLog(
            ticketNo,
            request.getLogDetails()
        );
        return ResponseEntity.ok(resolvedTicket);
    }
    
    @DeleteMapping("/{ticketNo}/delete")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketNo) {
        ticketService.deleteTicket(ticketNo);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
    

}