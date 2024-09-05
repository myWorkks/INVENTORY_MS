package com.bharath.inventory.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCategoryRequest {
	private String categoryName;
	private List<AddCategoryRequest> subCategory;
}
