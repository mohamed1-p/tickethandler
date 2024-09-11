package com.tickethandler.model;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
		
		
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("Requesters")
public class Requester extends UserEntity{


    @ManyToOne
    @JoinColumn(name = "requesterCompanyID")
    private Company company;
    
 

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;



	
    
    
    
    

}
