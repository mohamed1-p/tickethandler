package com.tickethandler.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CompaniesProductId  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="CompanyID")
	private int companyId;
	
	@Column(name="ProductID")
	private int productId;
	
}
