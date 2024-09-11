package com.tickethandler.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

