package com.bharath.inventory.service.classes;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bharath.inventory.entity.ProductDetails;
import com.bharath.inventory.model.AddProductDTO;
import com.bharath.inventory.repository.ProductRepository;
import com.bharath.inventory.service.interfaces.ProductService;

@Service(value = "productServiceImpl")
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Override
	public void addProduct(AddProductDTO productDTO) {

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProductName(productDTO.getProductName());
		productDetails.setDiscount(productDTO.getDiscount());
		productDetails.setPrice(productDTO.getPrice());
		productDetails.setCategory(productDTO.getCategory());
		productDetails.setProductDescription(productDTO.getDescription());
		productDetails.setAddedAt(LocalDateTime.now());

		productRepository.save(productDetails);
	}

}
