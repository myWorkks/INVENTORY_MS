package com.bharath.inventory.service.classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bharath.inventory.controller.ms.AssetManagementService;
import com.bharath.inventory.entity.Asset;
import com.bharath.inventory.entity.ProductDetails;
import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.AddProductDTO;
import com.bharath.inventory.model.AssetDTO;
import com.bharath.inventory.model.AssetViewResponse;
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
private static final Logger LOGGER=LoggerFactory.getLogger(ProductServiceImpl.class);
	@Override
	public void addProduct(AddProductDTO productDTO, Long userId) throws InventoryServiceException {
		LOGGER.info("add product method called with productDto :{} and user id :{}",productDTO,userId);
		List<ProductDetails> addedProductsByUser = productRepository.findByAddedByUserId(userId);

		if (!addedProductsByUser.isEmpty()) {
			Optional<ProductDetails> optional = addedProductsByUser.stream()
					.filter(product -> product.getSKU().equals(productDTO.getSKU())).findAny();

			if (optional.isPresent())
				throw new InventoryServiceException(InventoryExceptionMessages.PRODUCT_ALREADY_ADDED);
		}

		if (productDTO.getAssetInfo() != null) {
			uploadImages(productDTO.getAssetInfo());

		}

		productRepository.save(convertFromAddProductRequestToEntity(productDTO, userId));
	}

	private void uploadImages(List<AssetDTO> assetInfo) {
		;
		assetManagementService.uploadImages(assetInfo.stream().map(assetDTO -> assetDTO.getFile()).toList());
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

		List<Asset> assets = product.getAssetInfo();
		List<AssetViewResponse> assetViewResponses = assets.stream()
				.map(asset -> mapToAssetViewResponseFromAsset(asset, product.getProductId()))
				.collect(Collectors.toList());
		viewProduct.setFiles(assetViewResponses);
		return viewProduct;
	}

	private AssetViewResponse mapToAssetViewResponseFromAsset(Asset asset, Long productId) {
		AssetViewResponse assetViewResponse = new AssetViewResponse();
		assetViewResponse.setImage(asset.isImage());
		assetViewResponse.setFilePath(asset.getFilePath());
		assetViewResponse.setPrimary(asset.isPrimary());
		assetViewResponse.setVideo(asset.isVideo());
		assetViewResponse.setAssetId(asset.getAssetId());
		assetViewResponse.setProductId(productId);
		return assetViewResponse;
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
		List<AssetDTO> assetDTOs = product.getAssetInfo();
		List<Asset> assets = assetDTOs.stream().map(assetDTO -> mapToAssetFromAssetDTO(assetDTO, userId))
				.collect(Collectors.toList());
		viewProduct.setAssetInfo(assets);

		return viewProduct;
	}

	private Asset mapToAssetFromAssetDTO(AssetDTO assetDTO, Long userId) {
		Asset asset = new Asset();
		asset.setAddedAt(LocalDateTime.now());
		asset.setUpdatedAt(LocalDateTime.now());
		asset.setAddedBy(userId);
		asset.setImage(assetDTO.isImage());
		// https://bharathse-commerce.s3.ap-south-1.amazonaws.com/ProductImages/ironman.jpeg
		final String basePath = InventoryServiceConstants.S3_BUCKET_PRODUCT_PATH;
		asset.setFilePath(basePath.concat(assetDTO.getFile().getOriginalFilename()));
		asset.setPrimary(assetDTO.isPrimary());
		asset.setVideo(assetDTO.isVideo());
		return asset;
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

	@Override
	public String deleteInventory(Long userId) throws InventoryServiceException {
		List<ProductDetails> products = productRepository.findByAddedByUserId(userId);
		if (products.isEmpty())
			throw new InventoryServiceException(InventoryExceptionMessages.PRODUCTS_NOT_FOUND);
		productRepository.deleteAll(products);
		return String.format("%d " + InventoryServiceConstants.DELETE_MESSAGE, products.size());
	}

	@Override
	public String deleteProducts(Long userId, List<Long> productIds) throws InventoryServiceException {
		List<ProductDetails> products = productRepository.findByAddedByUserIdAndProductIdIn(userId, productIds);
		if (products.isEmpty())
			throw new InventoryServiceException(InventoryExceptionMessages.PRODUCTS_NOT_AVAILABLE);
		productRepository.deleteAll(products);
		return String.format("%d " + InventoryServiceConstants.DELETE_MESSAGE, products.size());
	}

	@Override
	public List<ViewProductResponse> viewAllProducts(Long userId, List<Long> productIds)
			throws InventoryServiceException {
		List<ProductDetails> products = null;
		if (productIds == null || productIds.isEmpty()) {
			products = productRepository.findByAddedByUserId(userId);

			if (products.isEmpty())
				throw new InventoryServiceException(InventoryExceptionMessages.PRODUCTS_NOT_FOUND);
		} else {
			products = productRepository.findByAddedByUserIdAndProductIdIn(userId, productIds);
			if (products.isEmpty())
				throw new InventoryServiceException(InventoryExceptionMessages.PRODUCTS_NOT_AVAILABLE);
		}
		if (products == null || products.isEmpty())
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