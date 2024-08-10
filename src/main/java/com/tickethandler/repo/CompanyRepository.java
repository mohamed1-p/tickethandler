package com.tickethandler.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Company;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>{

	List<Company> findByCompanyNameContainingIgnoreCase(String CompanyName);
}
