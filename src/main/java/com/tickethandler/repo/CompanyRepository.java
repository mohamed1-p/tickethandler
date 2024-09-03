package com.tickethandler.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Company;
import com.tickethandler.model.Product;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>{

	Page<Company> findByCompanyNameContainingIgnoreCase(String CompanyName,Pageable pageable);
//	 @Query("SELECT c.companyProducts FROM Company c WHERE c.id = :companyId")
//	List<Product> findProductsBycompanyId(@Param("companyId")int companyId);
	Page<Product> findProductBycompanyId(int companyId, Pageable pageable);
}
