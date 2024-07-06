package com.bharath.inventory.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long categoryId;
private String categoryName;
@OneToMany(cascade = CascadeType.ALL)
@JoinColumn(name="subcategory_id")
private List<Category> subCategory;
}
