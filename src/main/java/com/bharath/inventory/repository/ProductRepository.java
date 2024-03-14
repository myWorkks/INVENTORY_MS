package com.bharath.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bharath.inventory.entity.ProductDetails;

public interface ProductRepository extends JpaRepository<ProductDetails, Long> {
	ProductDetails findBySKU(String SKU);

	List<ProductDetails> findByAddedByUserId(Long userId);

	@Query(value = "SELECT * FROM product_details " + "WHERE " + "    brand LIKE CONCAT('%', ?1, '%') "
			+ "    OR model LIKE CONCAT('%', ?1, '%') " + "    OR product_description LIKE CONCAT('%', ?1, '%') "
			+ "    OR product_name LIKE CONCAT('%', ?1, '%') "
			+ "    OR tags LIKE CONCAT('%', ?1, '%') OR category LIKE CONCAT('%', ?1, '%') OR sub_category LIKE CONCAT('%', ?1, '%');", nativeQuery = true)
	List<ProductDetails> findBySearchKeyword(
			String searchKeyword);

}
