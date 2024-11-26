package com.interverse.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="cart")
public class Cart {
	
	@EmbeddedId
	private CartId cartId;
	
	@Column(name = "quantity")
	private Integer vol;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productsId")
	@JoinColumn(name = "product_id")
	private Product products;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("usersId")
	@JoinColumn(name = "user_id")
	private User users;

}
