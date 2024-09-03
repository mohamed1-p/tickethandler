package com.tickethandler.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.CompaniesProductId;
import com.tickethandler.model.CompaniesProducts;
import com.tickethandler.model.Company;
import com.tickethandler.model.Product;

@Repository
public interface CompanyProductsRepository extends 
JpaRepository<CompaniesProducts, CompaniesProductId>{
//	Optional<Company> findCompanyByproductId(int productId);
//	Optional<Product> findProductBycompanyId(int companyId);
	

}
