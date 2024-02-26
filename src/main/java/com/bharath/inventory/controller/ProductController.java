package com.bharath.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.inventory.model.AddProductDTO;
import com.bharath.inventory.service.interfaces.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	
	public ResponseEntity<String> addProduct(@RequestBody AddProductDTO productDTO){
		return null;
	}
}
