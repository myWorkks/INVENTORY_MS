package com.bharath.inventory.controller.ms;

import java.util.List;

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
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bharath.inventory.exception.InventoryServiceException;
import com.bharath.inventory.model.MultiPartInputRequest;
import com.bharath.inventory.utility.InventoryServiceConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

import reactor.core.publisher.Mono;

@Service(value = "assetMS")
public class AssetManagementServiceImpl implements AssetManagementService {

	@Autowired
	private WebClient.Builder webclient;

	@Override
	public String uploadImages(List<MultipartFile> images) {

		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();

		for (MultipartFile image : images) {

			formData.add("files", image.getResource());

		}
		return webclient.baseUrl(InventoryServiceConstants.ASSET_MS_URL).build().post()

				.uri(InventoryServiceConstants.UPLOAD_IMAGE_PATH).contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(formData)).retrieve().bodyToMono(String.class)
				.onErrorResume(WebClientResponseException.class, ex -> {
					String responseCode = String.valueOf(ex.getRawStatusCode());
					if (responseCode.startsWith("2"))
						return Mono.empty();
					else if (responseCode.equals("400")) {

						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode jsonNode = null;
						try {
							jsonNode = objectMapper.readTree(ex.getResponseBodyAsString());
						} catch (JsonProcessingException e) {
							throw new RuntimeException(e);
						}
						String errorMessage = jsonNode.get("errorMessage").asText();
						return Mono.error(() -> new InventoryServiceException(errorMessage));
					}

				else
						return Mono.error(ex);
				}).block();

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