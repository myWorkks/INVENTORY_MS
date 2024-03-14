package com.bharath.inventory.service.classes;

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
	public void addProduct(AddProductDTO productDTO, Long userId) throws InventoryServiceException {
		List<ProductDetails> addedProductsByUser = productRepository.findByAddedByUserId(userId);

		if (!addedProductsByUser.isEmpty()) {
			Optional<ProductDetails> optional = addedProductsByUser.stream()
					.filter(product -> product.getSKU().equals(productDTO.getSKU())).findAny();

			if (optional.isPresent())
				throw new InventoryServiceException(InventoryExceptionMessages.PRODUCT_ALREADY_ADDED);
		}
		if (productDTO.getProductImages() != null)
			assetManagementService.uploadImages(productDTO.getProductImages());
		productRepository.save(convertFromAddProductRequestToEntity(productDTO, userId));
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
	if(product.getImagePaths()!=null) {
		List<String> imagePaths = new ArrayList<String>();
		for (String path : product.getImagePaths().split(",")) {
			if (!path.trim().isEmpty())
				imagePaths.add(path);
		}
		viewProduct.setImagePaths(imagePaths);
	}
		return viewProduct;
	}

	private ProductDetails convertFromAddProductRequestToEntity(AddProductDTO product, Long userId) {
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
		viewProduct.setAddedByUserId(userId);
		MultipartFile[] images = product.getProductImages();
		if (images != null) {
			// https://bharathse-commerce.s3.ap-south-1.amazonaws.com/ProductImages/ironman.jpeg

			// https://bharathse-commerce.s3.ap-south-1.amazonaws.com/ProductImages/smiley.jpeg
			final String basePath = InventoryServiceConstants.S3_BUCKET_PRODUCT_PATH;

			StringBuilder imagePath = new StringBuilder();
			for (MultipartFile file : images) {
				imagePath.append(basePath).append(file.getOriginalFilename()).append(",");
			}
			viewProduct.setImagePaths(imagePath.toString());
		}
		return viewProduct;
	}

	@Override
	public List<ViewProductResponse> viewAllProducts() throws InventoryServiceException {
		List<ProductDetails> products = productRepository.findAll();
		if (products.isEmpty())
			throw new InventoryServiceException(InventoryExceptionMessages.PRODUCTS_NOT_FOUND);
		return products.stream().map(p -> convertFromProductEntityToViewResponse(p)).toList();

	}

	@Override
	public List<ViewProductResponse> searchProducts(String searchKeyword) throws InventoryServiceException {
		List<ProductDetails> products = productRepository.findBySearchKeyword(searchKeyword);
		if (products.isEmpty())
			throw new InventoryServiceException(InventoryExceptionMessages.SEARCH_PRODUCTS_NOT_FOUND);
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