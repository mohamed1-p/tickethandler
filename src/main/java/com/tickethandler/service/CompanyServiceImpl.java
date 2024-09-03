package com.tickethandler.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tickethandler.dto.CompanyDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.model.Company;
import com.tickethandler.model.Product;
import com.tickethandler.repo.CompanyRepository;
import com.tickethandler.repo.ProductRepository;


@Service
public class CompanyServiceImpl implements CompanyService{
	
	private CompanyRepository companyRepository;
	
	public CompanyServiceImpl(CompanyRepository companyRepository){
		this.companyRepository=companyRepository;
		
	}

	@Override
	public CompanyDto createCompany(String companyName) {
			
			Company company = new Company();
			
			company.setCompanyName(companyName);
			companyRepository.save(company);
			CompanyDto companyDto = mapCompanyToDto(company); 
			return companyDto;
	}

	@Override
	public ResponsePage<CompanyDto> getAllCompanies(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Company> companyPage = companyRepository.findAll(pageable);
		List<Company> companies = companyPage.getContent();
		
		List<CompanyDto> companyDto = companies.stream()
				.map(this::mapCompanyToDto)
				.collect(Collectors.toList());
		
		return mapDtoToPage(companyPage,companyDto);
	}

	@Override
	public ResponsePage<CompanyDto> getCompanyByName(String companyName,int pageNo,int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Company> companyPage = companyRepository.findByCompanyNameContainingIgnoreCase(
				companyName,pageable);
		List<Company> companies = companyPage.getContent();
		
		List<CompanyDto> companyDto = companies.stream()
				.map(this::mapCompanyToDto)
				.collect(Collectors.toList());
		
		return mapDtoToPage(companyPage,companyDto);
	}
	
	
	
	
	
	
	private CompanyDto mapCompanyToDto(Company company) {
		CompanyDto dto = new CompanyDto();
		dto.setId(company.getCompanyId());
		dto.setName(company.getCompanyName());
		dto.setProducts(company.getCompanyProducts().toString());
		return dto;
	}
	
	private ResponsePage<CompanyDto> mapDtoToPage(Page<Company> companyPage,
			List<CompanyDto> dto){
		ResponsePage<CompanyDto> content = new ResponsePage<>();
		
		content.setContent(dto);
		content.setPage(companyPage.getNumber());
		content.setSize(companyPage.getSize());
		content.setTotalElements(companyPage.getTotalElements());
		content.setTotalpages(companyPage.getTotalPages());
		content.setLast(companyPage.isLast());
		
		return content;
	}


}
