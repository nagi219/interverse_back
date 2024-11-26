package com.interverse.demo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "product_images")
public class ProductPhotos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "photo_name")
	private String photoName;
	
	@Column(name = "photo_path")
	private String photoPath;
	@Column(name = "added")
	private LocalDateTime added;
	
	

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "product_id")
	private Product products;
	
	@PrePersist
    public void onCreate() {
        if (added == null) {
            added = LocalDateTime.now();
        }
    }
	
	
	
	
}
