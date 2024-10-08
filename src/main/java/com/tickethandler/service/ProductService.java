package com.tickethandler.service;



import java.util.List;

import com.tickethandler.dto.ProductDto;
import com.tickethandler.dto.ResponsePage;

public interface ProductService {

	public ProductDto createProduct(ProductDto productDto);
	public ResponsePage<ProductDto> getAllProducts(int pageNo, int pageSize);
	public List<ProductDto> getProductbyCompanyId(int pageNo, int pageSize);
	public ResponsePage<ProductDto> getByProductAndCompanyNames(String companyName,
			String productName,int pageNo, int pageSize);
}
