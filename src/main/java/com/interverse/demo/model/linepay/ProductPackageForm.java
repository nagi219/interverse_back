package com.interverse.demo.model.linepay;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPackageForm {

	private String id;
	
	private String name;
	
	private Integer amount;
	
	private List<ProductForm> products;
	
	
	
}
