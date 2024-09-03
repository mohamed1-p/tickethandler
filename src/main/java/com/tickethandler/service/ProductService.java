package com.tickethandler.service;



import java.util.List;

import com.tickethandler.dto.ProductDto;
import com.tickethandler.dto.ResponsePage;

public interface ProductService {

	public ProductDto createProduct(ProductDto productDto);
	public ResponsePage<ProductDto> getAllProducts(int pageNo, int pageSize);
	public List<ProductDto> getProductbyCompanyId(int companyId, int pageNo, int pageSize);
	
}
