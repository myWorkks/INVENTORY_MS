package com.bharath.inventory.model;

import java.util.List;

import lombok.Data;

@Data
public class CategoryViewResponse {
	private Long categoryId;
	private String categoryName;
	private List<CategoryViewResponse> subCategories;
}
