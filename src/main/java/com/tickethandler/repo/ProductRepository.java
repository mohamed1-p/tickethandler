package com.tickethandler.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Company;
import com.tickethandler.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	 @Query(value = "SELECT p.* FROM ticket_handle.company_product cp " +
             "INNER JOIN ticket_handle.companies c ON c.companyid = cp.company_id " +
             "INNER JOIN ticket_handle.products p ON p.productid = cp.product_id " +
             "WHERE p.product_name LIKE :productName AND c.companyname LIKE :companyName",
     nativeQuery = true)
	 Page<Product> findProductsByCompanyAndNameLike(@Param("productName") String productName, 
             @Param("companyName") String companyName,Pageable pageable);
}
