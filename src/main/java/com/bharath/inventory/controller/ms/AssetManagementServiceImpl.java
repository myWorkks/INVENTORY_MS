package com.bharath.inventory.controller.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.bharath.inventory.model.MultiPartInputRequest;
import com.bharath.inventory.utility.InventoryServiceConstants;
import com.google.common.net.HttpHeaders;

@Service(value = "assetMS")
public class AssetManagementServiceImpl implements AssetManagementService {

	@Autowired
	private WebClient.Builder webclient;

	@Override
	public String uploadImages(MultipartFile[] images) {

		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		
		for (MultipartFile image : images) {

			formData.add("files", image.getResource());

		}
		return  webclient.baseUrl(InventoryServiceConstants.ASSET_MS_URL).build().post()

				.uri(InventoryServiceConstants.UPLOAD_IMAGE_PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(formData))
				.retrieve()
				.bodyToMono(String.class)
				.block();

	

	}

}
//.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.toString())
// .bodyValue(multiPartInputRequest)
//builder.part("files", new ByteArrayResource(image.getBytes())).header(HttpHeaders.CONTENT_DISPOSITION, header);
//RestTemplate restTemplate = new RestTemplate();
//ResponseEntity<String> responseEntity = restTemplate.postForEntity(
//        InventoryServiceConstants.ASSET_MS_URL + InventoryServiceConstants.UPLOAD_IMAGE_PATH,
//        requestBody,
//        String.class);