package com.tickethandler.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ProductID")
	private int productId;
	
	@Column(nullable = false, length = 500)
    private String productName;

//	@OneToMany(mappedBy = "product")
//	private List<Company> companyProducts = new ArrayList<>();
	
//	@ManyToMany(mappedBy = "companyProducts",fetch =FetchType.EAGER)
//    private List<Company> companies = new ArrayList<>();

	 @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Ticket> tickets;
}











