package com.bharath.inventory.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bharath.inventory.entity.Unit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductDTO {

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

	private List<AssetDTO> assetInfo;

}
