package com.bharath.inventory.controller.ms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AssetManagementService {
public String uploadImages(List<MultipartFile> images) ;
}
