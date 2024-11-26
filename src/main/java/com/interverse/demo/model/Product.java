package com.interverse.demo.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "product_name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "color")
	private String color;
	@Column(name = "price")
	private Integer price;
	

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "added")
	private LocalDateTime addtime;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category categories;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "products")
	private List<ProductPhotos> productPhotos = new LinkedList<>();
	
	
	
	@PrePersist // 當物件要進入 Persistent 狀態以前，先做這個方法
	public void onCreate() {
		if (addtime == null) {
			addtime = LocalDateTime.now() ;
		}
	}
	
}
