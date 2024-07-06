package com.bharath.inventory.model;

import java.time.LocalDateTime;
import java.util.List;

import com.bharath.inventory.entity.Unit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewProductResponse {

	private Long productId;
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

	private Unit unit;

	private LocalDateTime addedAt;
	private LocalDateTime modifiedAt;

	private Long addedByUserId;

	private List<AssetViewResponse> files;

}
