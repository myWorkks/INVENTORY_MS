package com.bharath.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.AddCategoryRequest;
import com.bharath.inventory.model.CategoryViewResponse;
import com.bharath.inventory.service.interfaces.CategoryService;

@RequestMapping(value = "category")
@RestController
@CrossOrigin
@Validated
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping(value = "add-category")
	private ResponseEntity<String> addCategory(@RequestBody AddCategoryRequest addCategoryRequest,
			@RequestParam(required = true, name = "userId") Long userId) throws InventoryServiceException {
		return new ResponseEntity<String>(categoryService.addCategory(addCategoryRequest, userId), HttpStatus.CREATED);
	}

	@GetMapping(value = "view-category")
	private ResponseEntity<List<CategoryViewResponse>> viewCategoryTree() {
		return new ResponseEntity<List<CategoryViewResponse>>(categoryService.viewCategoryTree(), HttpStatus.OK);
	}

}
