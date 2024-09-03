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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Companies")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CompanyID")
	private int companyId;
	
	 @Column(name="companyname",nullable = false, length = 500)
	 private String companyName;
	

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
    
    @ManyToMany(fetch = FetchType.EAGER)
   	@JoinTable(name = "company_product", joinColumns= @JoinColumn
   			  (name = "company_id",referencedColumnName = "CompanyID"),
   			  inverseJoinColumns = @JoinColumn(name = "product_id",
   			  referencedColumnName = "ProductID"))
    private List<Product> companyProducts = new ArrayList<>();
    
//	@OneToMany(mappedBy = "company")
// private List<CompaniesProducts> companyProductss = new ArrayList<>();

}
