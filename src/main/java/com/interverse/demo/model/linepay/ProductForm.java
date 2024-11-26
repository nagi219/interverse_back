package com.interverse.demo.model.linepay;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductForm {

	private String id;
	
	private String name;
	
	private String imageUrl;
	
	private Integer quantity;
	
	private Integer price;
	
}
