package com.bharath.inventory.service.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.inventory.entity.Category;
import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.AddCategoryRequest;
import com.bharath.inventory.model.CategoryViewResponse;
import com.bharath.inventory.repository.CategoryRepository;
import com.bharath.inventory.service.interfaces.CategoryService;
import com.bharath.inventory.utility.InventoryExceptionMessages;

@Service
@Transactional(rollbackForClassName = { "Exception" })
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public String addCategory(AddCategoryRequest addCategoryRequest, Long userId) throws InventoryServiceException {
		Optional<Category> optionalCategory = categoryRepository
				.findByCategoryName(addCategoryRequest.getCategoryName());
		if (optionalCategory.isPresent()) {
			throw new InventoryServiceException(InventoryExceptionMessages.CATEGORY_ADDED);
		}
		categoryRepository.save(saveCategory(addCategoryRequest));
		return "CATEGORY ADDED SUCCESSFULLY";
	}

	public Category saveCategory(AddCategoryRequest request) {
		Category category = new Category();
		category.setCategoryName(request.getCategoryName());
		List<Category> subCategoryList = new ArrayList<>();

		if (request.getSubCategory() != null) {
			for (AddCategoryRequest subRequest : request.getSubCategory()) {
				if (subRequest != null) {
					Category subCategory = saveCategory(subRequest);
					subCategoryList.add(subCategory);
				}
			}
		}

		category.setSubCategory(subCategoryList);
		return category;
	}

//	private CategoryViewResponse populateCategoryTree(CategoryViewResponse categoryViewResponse) {
//
//		List<Category> rootCategories = categoryRepository.findBySubCategoryIsNull();
//
//		List<CategoryViewResponse> subCategories = new ArrayList<>();
//
//		for (Category category : rootCategories) {
//			CategoryViewResponse subCategoryResponse = new CategoryViewResponse();
//			subCategoryResponse.setCategoryId(category.getCategoryId());
//			subCategoryResponse.setSubCategories(populateSubcategories(category));
//
//			subCategories.add(subCategoryResponse);
//		}
//
//		categoryViewResponse.setSubCategories(subCategories);
//
//		return categoryViewResponse;
//	}
//
//	private List<CategoryViewResponse> populateSubcategories(Category category) {
//		List<CategoryViewResponse> subCategoryList = new ArrayList<>();
//
//		List<Category> subcategories = category.getSubCategory();
//
//		for (Category subcategory : subcategories) {
//			CategoryViewResponse subCategoryResponse = new CategoryViewResponse();
//			subCategoryResponse.setCategoryId(subcategory.getCategoryId());
//			subCategoryResponse.setSubCategories(populateSubcategories(subcategory));
//
//			subCategoryList.add(subCategoryResponse);
//		}
//
//		return subCategoryList;
//	}

	@Override
	public List<CategoryViewResponse> viewCategoryTree() {

		List<Category> parentcategories = categoryRepository.findBySubCategoryIsNotNull();

		return getCategoryViewResponse(parentcategories);

	}

	private List<CategoryViewResponse> getCategoryViewResponse(List<Category> categories) {

		List<CategoryViewResponse> categoryViewResponseList = new ArrayList<CategoryViewResponse>();

		if (categories != null && !categories.isEmpty()) {
			for (Category category : categories) {

				CategoryViewResponse categoryViewResponse = new CategoryViewResponse();
				categoryViewResponse.setCategoryId(category.getCategoryId());
				categoryViewResponse.setCategoryName(category.getCategoryName());

				if (category.getSubCategory() != null) {

					categoryViewResponse.setSubCategories(getCategoryViewResponse(category.getSubCategory()));

				}
				categoryViewResponseList.add(categoryViewResponse);
			}

		}
		return categoryViewResponseList;
	}

}
