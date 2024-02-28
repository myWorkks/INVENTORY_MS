package com.bharath.inventory.service.classes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bharath.inventory.controller.ms.AssetManagementService;
import com.bharath.inventory.entity.ProductDetails;
import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.AddProductDTO;
import com.bharath.inventory.model.ViewProductResponse;
import com.bharath.inventory.repository.ProductRepository;
import com.bharath.inventory.service.interfaces.ProductService;
import com.bharath.inventory.utility.InventoryExceptionMessages;
import com.bharath.inventory.utility.InventoryServiceConstants;

@Service(value = "productServiceImpl")
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private AssetManagementService assetManagementService;

	@Override
	public void addProduct(AddProductDTO productDTO) throws InventoryServiceException {
	
		ProductDetails product = productRepository.findBySKU(productDTO.getSKU());

		
		if (product != null)
			throw new InventoryServiceException(InventoryExceptionMessages.PRODUCT_ALREADY_ADDED);
		assetManagementService.uploadImages(productDTO.getProductImages());
		productRepository.save(convertFromAddProductRequestToEntity(productDTO));
	}

	@Override
	public ViewProductResponse viewProduct(Long pid) throws InventoryServiceException {
		Optional<ProductDetails> optProduct = productRepository.findById(pid);
		ProductDetails product = optProduct
				.orElseThrow(() -> new InventoryServiceException(InventoryExceptionMessages.PRODUCT_NOT_FOUND));

		return convertFromProductEntityToViewResponse(product);
	}

	private ViewProductResponse convertFromProductEntityToViewResponse(ProductDetails product) {
		ViewProductResponse viewProduct = new ViewProductResponse();
		viewProduct.setAddedAt(product.getAddedAt());
		viewProduct.setProductId(product.getProductId());
		viewProduct.setProductName(product.getProductName());
		viewProduct.setProductDescription(product.getProductDescription());
		viewProduct.setSKU(product.getSKU());
		viewProduct.setCategory(product.getCategory());
		viewProduct.setSubCategory(product.getSubCategory());
		viewProduct.setBrand(product.getBrand());
		viewProduct.setModel(product.getModel());
		viewProduct.setTags(product.getTags());
		viewProduct.setPrice(product.getPrice());
		viewProduct.setCurrency(product.getCurrency());
		viewProduct.setTaxRate(product.getTaxRate());
		viewProduct.setDiscount(product.getDiscount());
		viewProduct.setQuantityAvailable(product.getQuantityAvailable());
		viewProduct.setMinStockLevel(product.getMinStockLevel());
		viewProduct.setMaxStockLevel(product.getMaxStockLevel());
		viewProduct.setRecorderPoint(product.getRecorderPoint());
		viewProduct.setUnit(product.getUnit());
		viewProduct.setModifiedAt(product.getModifiedAt());
		viewProduct.setAddedByUserId(product.getAddedByUserId());
		List<String > imagePaths=new ArrayList<String>();
	for(String path:	product.getImagePaths().split(",")) {
		if(!path.trim().isEmpty())
			imagePaths.add(path);
	}
		viewProduct.setImagePaths(imagePaths);
		return viewProduct;
	}

	private ProductDetails convertFromAddProductRequestToEntity(AddProductDTO product) {
		ProductDetails viewProduct = new ProductDetails();
		viewProduct.setAddedAt(LocalDateTime.now());
		viewProduct.setProductName(product.getProductName());
		viewProduct.setProductDescription(product.getProductDescription());
		viewProduct.setSKU(product.getSKU());
		viewProduct.setCategory(product.getCategory());
		viewProduct.setSubCategory(product.getSubCategory());
		viewProduct.setBrand(product.getBrand());
		viewProduct.setModel(product.getModel());
		viewProduct.setTags(product.getTags());
		viewProduct.setPrice(product.getPrice());
		viewProduct.setCurrency(product.getCurrency());
		viewProduct.setTaxRate(product.getTaxRate());
		viewProduct.setDiscount(product.getDiscount());
		viewProduct.setQuantityAvailable(product.getQuantityAvailable());
		viewProduct.setMinStockLevel(product.getMinStockLevel());
		viewProduct.setMaxStockLevel(product.getMaxStockLevel());
		viewProduct.setRecorderPoint(product.getRecorderPoint());
		viewProduct.setUnit(product.getUnit());
		viewProduct.setModifiedAt(LocalDateTime.now());
		viewProduct.setAddedByUserId(product.getAddedByUserId());
		MultipartFile[] images = product.getProductImages();
		//https://bharathse-commerce.s3.ap-south-1.amazonaws.com/ProductImages/ironman.jpeg
		
		//https://bharathse-commerce.s3.ap-south-1.amazonaws.com/ProductImages/smiley.jpeg
		final 
		String basePath = InventoryServiceConstants.S3_BUCKET_PRODUCT_PATH;
		
		StringBuilder imagePath = new StringBuilder();
		for (MultipartFile file : images) {
			imagePath.append(basePath).append(file.getOriginalFilename()).append(",");
		}
		viewProduct.setImagePaths(imagePath.toString());
		return viewProduct;
	}

	@Override
	public List<ViewProductResponse> viewAllProducts() throws InventoryServiceException {
		List<ProductDetails> products = productRepository.findAll();
		if (products.isEmpty())
			throw new InventoryServiceException(InventoryExceptionMessages.PRODUCTS_NOT_FOUND);
		return products.stream().map(p -> convertFromProductEntityToViewResponse(p)).toList();

	}

}
//MultipartFile[] images = productDTO.getProductImages();
////MultipartBodyBuilder builder = new MultipartBodyBuilder();
//HttpHeaders headers = new HttpHeaders();
//headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//MultiValueMap<String, MultipartFile> formData = new LinkedMultiValueMap<String, MultipartFile>();
//for (MultipartFile image : images) {
//	// builder.part("files", image);
//	formData.add("files", image);
//}
//HttpEntity<MultiValueMap<String, MultipartFile>> requestBody = new HttpEntity<MultiValueMap<String, MultipartFile>>(formData,
//		headers);