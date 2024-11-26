package com.interverse.demo.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
	 	private Integer userId;
	    private Integer productId;
	    private Integer vol;
	    private Integer price;
	    private String productName;
	    
	    
}
