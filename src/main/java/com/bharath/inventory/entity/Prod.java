package com.bharath.inventory.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Prod {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pid;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private Category category;
}
