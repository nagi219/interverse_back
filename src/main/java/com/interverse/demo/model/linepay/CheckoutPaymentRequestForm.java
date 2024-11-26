package com.interverse.demo.model.linepay;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckoutPaymentRequestForm {

	
	private Integer Amount;
	
	private String Currency;
	
	private String OrderId;
	
	private List<ProductPackageForm> Packages;
	
	private RedirectUrls RedirectUrls;
}
