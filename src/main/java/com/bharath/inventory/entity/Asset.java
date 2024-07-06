package com.bharath.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Asset {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long assetId;
	private boolean isImage;
	private boolean isPrimary;
	private boolean isVideo;
	private LocalDateTime addedAt;
	private LocalDateTime updatedAt;
	private Long addedBy;
	private String filePath;
}
