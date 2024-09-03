package com.tickethandler.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity

@DiscriminatorValue("SupportEngineers")
public class SupportEngineer extends UserEntity {



    @OneToMany(mappedBy = "assigendTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> assignedTickets;

    @OneToMany(mappedBy = "resolvedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> resolvedTickets;
    
    @OneToMany(mappedBy = "logedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketsLog> loggedTickets;

   


  
    
    
    
    
    
    
    
    
}
