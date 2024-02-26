package com.bharath.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.inventory.entity.ProductDetails;

public interface ProductRepository extends JpaRepository<ProductDetails, Long> {

}
