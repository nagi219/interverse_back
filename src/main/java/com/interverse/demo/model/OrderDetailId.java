package com.interverse.demo.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderDetailId implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "order_id")
	private Integer ordersId;
	@Column(name = "product_id")
	private Integer productsId;
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(ordersId, productsId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetailId other = (OrderDetailId) obj;
		return Objects.equals(ordersId, other.ordersId) && Objects.equals(productsId, other.productsId);
	}


	
	
}