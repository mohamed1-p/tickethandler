package com.tickethandler.service;

import com.tickethandler.dto.CompanyDto;
import com.tickethandler.dto.ResponsePage;

public interface CompanyService {
	
	public CompanyDto createCompany(String companyName);
	public ResponsePage<CompanyDto> getAllCompanies(int pageNo, int pageSize);
	public ResponsePage<CompanyDto> getCompanyByName(String companyName,int pageNo,int pageSize);
	public CompanyDto getCompanyByid();
	
}
