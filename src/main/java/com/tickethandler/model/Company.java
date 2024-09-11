package com.tickethandler.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    
    @ManyToMany(fetch = FetchType.LAZY)
   	@JoinTable(name = "company_product", joinColumns= @JoinColumn
   			  (name = "company_id",referencedColumnName = "CompanyID"),
   			  inverseJoinColumns = @JoinColumn(name = "product_id",
   			  referencedColumnName = "ProductID"))
    private Set<Product> companyProducts = new HashSet<>();
    
//	@OneToMany(mappedBy = "company")
// private List<CompaniesProducts> companyProductss = new ArrayList<>();

}
