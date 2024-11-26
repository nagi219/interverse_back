package com.interverse.demo.model.linepay;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RedirectUrls {

	private String ConfirmUrl;
	
	private String AppPackageName;
	
	private String cancelUrl;
}
