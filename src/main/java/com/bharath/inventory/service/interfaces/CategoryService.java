package com.bharath.inventory.service.interfaces;

import java.util.List;

import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.AddCategoryRequest;
import com.bharath.inventory.model.CategoryViewResponse;

public interface CategoryService {

	String addCategory(AddCategoryRequest addCategoryRequest, Long userId) throws InventoryServiceException;

	List<CategoryViewResponse> viewCategoryTree();

}
