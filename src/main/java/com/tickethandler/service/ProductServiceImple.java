package com.tickethandler.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tickethandler.dto.CompanyDto;
import com.tickethandler.dto.ProductDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.Company;
import com.tickethandler.model.Product;
import com.tickethandler.model.Requester;
import com.tickethandler.repo.CompanyRepository;
import com.tickethandler.repo.ProductRepository;
import com.tickethandler.repo.UserRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductServiceImple implements ProductService {
	
	private ProductRepository productRepository;
	private CompanyRepository companyRepository;
	private UserRepository userRepository;

	
	public ProductServiceImple(ProductRepository productRepository,
			CompanyRepository companyRepository, UserRepository userRepository){
		this.userRepository=userRepository;
		this.companyRepository=companyRepository;
		this.productRepository=productRepository;
		
	}

	@Override
	public ProductDto createProduct(ProductDto productDto) {
		Product product = new Product();
		product.setProductName(productDto.getProductName());
		
		Company company = companyRepository.findById(productDto.getCompanyId())
				.orElseThrow(()-> new ResourceNotFoundException("Company not found"));
		
		
		company.getCompanyProducts().add(product);

		productRepository.save(product);
		companyRepository.save(company);
		
		
		return productDto;
	}

	@Override
	public ResponsePage<ProductDto> getAllProducts(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Product> productPage = productRepository.findAll(pageable);
		List<Product> products = productPage.getContent();
		
		List<ProductDto> productDto = products.stream()
				.map(this::mapProductToDto)
				.collect(Collectors.toList());
		return mapDtoToPage(productPage,productDto);
	}

	@Override
	public List<ProductDto> getProductbyCompanyId( int pageNo, int pageSize) {
//		Company company = companyRepository.findById(companyId)
//	            .orElseThrow(() -> new ResourceNotFoundException("Company not found")); 
		
		
//		 Pageable pageable = PageRequest.of(pageNo, pageSize);
//		 Page<Product> productPage = companyRepository.findProductBycompanyId(companyId, pageable);
//		 List<Product> products = productPage.getContent();
		 
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Requester requester = (Requester)userRepository.findByemail(username).
        		orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Company company = requester.getCompany();
        
        Set<Product> products = company.getCompanyProducts();
        
	        
		 List<ProductDto> productDto = products.stream()
					.map(this::mapProductToDto)
					.collect(Collectors.toList());
	
		
		 return productDto;
	
	}
	
	@Override
	public ResponsePage<ProductDto> getByProductAndCompanyNames(String companyName,
			String productName,int pageNo, int pageSize) {
		 String productNameLike = productName + "%";
	     String companyNameLike = companyName + "%";
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Product> productPage = productRepository.
				findProductsByCompanyAndNameLike(productNameLike,
						companyNameLike,pageable);
		List<Product> products = productPage.getContent();
		
		List<ProductDto> productDto = products.stream()
				.map(this::mapProductToDto)
				.collect(Collectors.toList());
		return mapDtoToPage(productPage,productDto);
	}

	private ProductDto mapProductToDto(Product product) {
		ProductDto dto = new ProductDto();
		dto.setProductName(product.getProductName());
	if(getCompaniesByProduct(product.getProductId()).size()==0) {
		
	}else {
		Company company = getCompaniesByProduct(product.getProductId()).get(0);
		dto.setCompanyId(company.getCompanyId());
		dto.setCompanyName(company.getCompanyName());
	}
	
		
	return dto;	
	}
	
	
	private ResponsePage<ProductDto> mapDtoToPage(Page<Product> productPage,
			List<ProductDto> dto){
		ResponsePage<ProductDto> content = new ResponsePage<>();
		
		content.setContent(dto);
		content.setPage(productPage.getNumber());
		content.setSize(productPage.getSize());
		content.setTotalElements(productPage.getTotalElements());
		content.setTotalpages(productPage.getTotalPages());
		content.setLast(productPage.isLast());
		
		return content;
	}
	
	
	
	private List<Company> getCompaniesByProduct(int productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        return companyRepository.findAll().stream()
            .filter(company -> company.getCompanyProducts().contains(product))
            .collect(Collectors.toList());
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
