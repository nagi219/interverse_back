package com.interverse.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {
	
	private Integer id;
	private String transactionNo;
	private String paymentMethod;
	private Long amount;
	private Short status;
	private LocalDateTime added;
	private Integer userId;

}
