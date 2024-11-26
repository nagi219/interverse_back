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
@Table(name="order_details")
public class OrderDetail {

	
	@EmbeddedId
	private OrderDetailId orderDetailId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("ordersId")
	@JoinColumn(name = "order_id")
	private Order orders;
	
	
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productsId")
	@JoinColumn(name = "product_id")
	private Product products;
	
	
}
