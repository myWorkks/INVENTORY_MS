package com.bharath.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
//@Document(indexName = "product_details")
@Getter
@Setter
public class ProductDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	//private String productId;
	private String productName;
	private String productDescription;
	private String SKU;

	private String category;

	private String subCategory;

	private String brand;
	private String model;
	private String tags;

	private Float price;
	private String currency;
	private Float taxRate;
	private Float discount;

	private Integer quantityAvailable;
	private Integer minStockLevel;
	private Integer maxStockLevel;
	private Integer recorderPoint;
	@Enumerated(EnumType.STRING)
	private Unit unit;

	private LocalDateTime addedAt;
	private LocalDateTime modifiedAt;

	private Long addedByUserId;

	@Lob
	private String imagePaths;

}
