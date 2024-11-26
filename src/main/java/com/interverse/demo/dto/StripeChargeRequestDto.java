package com.interverse.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StripeChargeRequestDto {
	private String token;
	private Long amount;

}
