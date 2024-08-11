package com.tickethandler.model;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "Requesters")
public class Requester{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequesterID")
    private int requesterId;

    @Column(nullable = false, length = 500)
    private String requesterName;
    
    @ManyToOne
    @JoinColumn(name = "requesterCompanyID", nullable = false)
    private Company company;
    
    @Column(nullable = false, length = 20)
    private String requesterMobileNo;

    @Column(nullable = false, length = 250)
    private String requesterEmail;

    @Column(nullable = false, length = 250)
    private String requesterPassword;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
    
    
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns= @JoinColumn
			  (name = "user_id",referencedColumnName = "requesterId"),
			  inverseJoinColumns = @JoinColumn(name = "Role_id",
			  referencedColumnName = "id"))
	private List<ReqeusterRole> roles = new ArrayList<>();

    
    

}
