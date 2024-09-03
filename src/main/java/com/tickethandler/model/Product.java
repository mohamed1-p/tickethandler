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











