package com.bharath.inventory.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AddProductDTO {
private String productName;
private Float price;
private Float discount;
private String description;
private String category;
private String subCategory;
private Integer quantity;
private MultipartFile[] productImages;
}
