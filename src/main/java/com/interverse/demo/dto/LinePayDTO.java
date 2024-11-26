package com.interverse.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinePayDTO {

	private String channelSecret;
	private String requestUri;
	private String nonce;
	private String signature;
	private String body;
	private String channelId;
	private String requestHttpUri;
	
	
	
}
