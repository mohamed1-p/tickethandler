package com.tickethandler.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticketslog")
public class TicketsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logNo;
    
    @ManyToOne
    @JoinColumn(name = "ticketNo", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private LocalDateTime logDate;

    @ManyToOne
    @JoinColumn(name = "logedBy", nullable = false)
    private SupportEngineer logedBy;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String logDetails;

}

