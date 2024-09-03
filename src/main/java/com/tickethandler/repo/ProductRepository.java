package com.tickethandler.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Company;
import com.tickethandler.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

//	 Page<Product> findByCompany(Company company, Pageable pageable);
}
