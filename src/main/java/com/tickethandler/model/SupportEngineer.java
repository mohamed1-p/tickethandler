package com.tickethandler.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SupportEngineers")
public class SupportEngineer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int engineerId;

    @Column(nullable = false, length = 500)
    private String engineerName;

    @Column(nullable = false, length = 20)
    private String engineerMobileNo;

    @Column(nullable = false, length = 250)
    private String engineerEmail;

    @Column(nullable = false, length = 250)
    private String engineerPassword;


    @OneToMany(mappedBy = "assigendTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> assignedTickets;

    @OneToMany(mappedBy = "resolvedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> resolvedTickets;
    
    @OneToMany(mappedBy = "logedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketsLog> loggedTickets;
}
