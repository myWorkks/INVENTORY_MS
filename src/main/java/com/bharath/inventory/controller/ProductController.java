package com.bharath.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.AddProductDTO;
import com.bharath.inventory.model.ViewProductResponse;
import com.bharath.inventory.service.interfaces.ProductService;
import com.bharath.inventory.utility.InventoryServiceConstants;

@RestController
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping(value = "product-add")
	public ResponseEntity<String> addProduct(@ModelAttribute AddProductDTO productDTO,
			@RequestParam(name = "userId", required = true) Long userId) throws InventoryServiceException {

		productService.addProduct(productDTO, userId);

		return new ResponseEntity<String>(InventoryServiceConstants.PRODUCT_ADD_SUCCESS, HttpStatus.CREATED);
	}

	@GetMapping(value = "product-view")
	public ResponseEntity<ViewProductResponse> viewProduct(@RequestParam(name = "pid", required = true) Long pid)
			throws InventoryServiceException {

		return new ResponseEntity<ViewProductResponse>(productService.viewProduct(pid), HttpStatus.OK);
	}

	@GetMapping(value = "product-view-all")
	public ResponseEntity<List<ViewProductResponse>> viewAllProducts() throws InventoryServiceException {

		return new ResponseEntity<List<ViewProductResponse>>(productService.viewAllProducts(), HttpStatus.OK);
	}

	@GetMapping(value = "product-search")
	public ResponseEntity<List<ViewProductResponse>> searchProducts(
			@RequestParam(required = true, name = "searchKeyword") String searchKeyword)
			throws InventoryServiceException {

		return new ResponseEntity<List<ViewProductResponse>>(productService.searchProducts(searchKeyword),
				HttpStatus.OK);
	}
}
