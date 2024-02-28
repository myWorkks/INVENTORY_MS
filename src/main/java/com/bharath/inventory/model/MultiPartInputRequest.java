package com.bharath.inventory.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MultiPartInputRequest {
private MultipartFile[] files;
}
