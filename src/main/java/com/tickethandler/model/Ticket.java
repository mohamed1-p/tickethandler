package com.tickethandler.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TicketNo")
    private Long ticketNo;
    
    private Date ticketDate;
    
    @ManyToOne
    @JoinColumn(name = "requesterId", nullable = false)
    private Requester requester;
    
    @ManyToOne
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;
    
    @ManyToOne
    @JoinColumn(name = "ticketType", nullable = false)
    private TicketType ticketType;
    
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
    
    @Column(nullable = false, length = 500)
    private String ticketSummary;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String ticketDetails;
    private int ticketPriority;
    
    @ManyToOne
    @JoinColumn(name = "ticketStatus", nullable = false)
    private TicketStatus ticketStatus;
    
    @ManyToOne
    @JoinColumn(name = "assigendTo")
    private SupportEngineer assigendTo;

    @ManyToOne
    @JoinColumn(name = "resolvedBy")
    private SupportEngineer resolvedBy;

    private LocalDateTime resolutionDate;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketsLog> ticketLogs;
    
}
