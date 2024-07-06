package com.bharath.inventory.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Data
public class AssetDTO {
private	MultipartFile file;
private boolean isImage;
private boolean isVideo;
private boolean isPrimary;
}
