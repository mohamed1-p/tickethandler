package com.tickethandler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.CompaniesProductId;
import com.tickethandler.model.CompaniesProducts;
import com.tickethandler.model.Product;

@Repository
public interface CompanyProductsRepository extends 
JpaRepository<CompaniesProducts, CompaniesProductId>{
	

}
