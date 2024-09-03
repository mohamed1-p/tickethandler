package com.tickethandler.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.CompanyDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.model.Company;
import com.tickethandler.service.CompanyService;

@RestController
@RequestMapping("api/company")
public class CompanyController {
	
	
	
	private CompanyService companyService;
	
	public CompanyController(CompanyService companyService) {
		this.companyService=companyService;
	}
	
	 @GetMapping
	    public ResponseEntity<ResponsePage<CompanyDto>> getAllCompanies(
	    		 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
				 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
		 ResponsePage<CompanyDto> companies = companyService.getAllCompanies(pageNo,pageSize);
	        return new ResponseEntity<>(companies, HttpStatus.OK);
	    }
	 
	 
	 
	 @PostMapping("/create")
	    public ResponseEntity<CompanyDto> createCompany(@RequestParam String companyName) {
	        CompanyDto createdCompany = companyService.createCompany(companyName);
	        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
	    }
	 
	 
	 
	 
	 @GetMapping("/name")
	    public ResponseEntity<ResponsePage<CompanyDto>> getCompanyById(@RequestParam String companyName,
	    		 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
				 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
	        ResponsePage<CompanyDto> company = companyService.getCompanyByName(companyName,pageNo,pageSize);
	        return new ResponseEntity<>(company, HttpStatus.OK);
	                   
	    }

}
