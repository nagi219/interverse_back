package com.interverse.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaypalDTO {

	private String scope;
	
	private String access_token;
	
	private String token_type;
	
	private String app_id;
	
	private Integer expires_in;
	
	private String nonce;
}
