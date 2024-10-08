package com.tickethandler.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.ProductDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.service.ProductService;

@RestController
@RequestMapping("api/product")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		
		this.productService = productService;
	}
	
	
	
	 @GetMapping
	    public ResponseEntity<ResponsePage<ProductDto>> getAllproducts(
	    		 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
				 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
		 ResponsePage<ProductDto> products = productService.getAllProducts(pageNo,pageSize);
	        return new ResponseEntity<>(products, HttpStatus.OK);
	    }
	 
	 @GetMapping("product-like-name")
	    public ResponseEntity<ResponsePage<ProductDto>> getByProductAndCompanyName(
	    		 @RequestParam String companyName,
	    		 @RequestParam String productName,
	    		 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
				 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
		 ResponsePage<ProductDto> products = productService.
				 getByProductAndCompanyNames(companyName,productName,pageNo,pageSize);
	        return new ResponseEntity<>(products, HttpStatus.OK);
	    }
	 
	 @GetMapping("/products")
	    public ResponseEntity<List<ProductDto>> getProductsByUser(
	    		 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
				 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
	        List<ProductDto> products= productService.getProductbyCompanyId(pageNo,pageSize) ;
	       
	        return new ResponseEntity<>(products, HttpStatus.OK);
	    }
	
	 
	 
	 @PostMapping("/create")
	    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) {
		 ProductDto createdProduct = productService.createProduct(dto);
		
	        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	    }
	 
	
}
