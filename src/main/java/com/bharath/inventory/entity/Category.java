package com.bharath.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
public class Category {
private Long categoryId;
private String category;
}
