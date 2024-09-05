package com.bharath.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bharath.inventory.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	Optional<Category> findByCategoryName(String categoryName);

	List<Category> findBySubCategoryIsNotNull();

}
