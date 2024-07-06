package com.bharath.inventory.service.interfaces;

import java.util.List;

import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.AddProductDTO;
import com.bharath.inventory.model.ViewProductResponse;

public interface ProductService {
public void addProduct(AddProductDTO productDTO,Long userId) throws InventoryServiceException;

public ViewProductResponse viewProduct(Long pid) throws InventoryServiceException;

public List<ViewProductResponse> viewAllProducts() throws InventoryServiceException;
public List<ViewProductResponse>searchProducts(String searchKeyword) throws InventoryServiceException;

public String deleteInventory(Long userId) throws InventoryServiceException;

public String deleteProducts(Long userId, List<Long> productIds) throws InventoryServiceException;

public List<ViewProductResponse> viewAllProducts(Long userId,List<Long> productIds) throws InventoryServiceException;
}
