package com.interverse.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.TransactionDto;
import com.interverse.demo.model.Transaction;
import com.interverse.demo.service.TransactionService;
import com.interverse.demo.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminFunctionController {
	
	@Autowired
	private TransactionService transService;
	
	@GetMapping("/transaction/all")
	public ResponseEntity<List<TransactionDto>> findAllTransaction() {

		List<TransactionDto> allTransaction = transService.findAllTransaction();
		return ResponseEntity.ok(allTransaction);
	}
	
	@GetMapping("/transaction/handling")
	public ResponseEntity<List<TransactionDto>> findHandlingTransaction() {

		List<TransactionDto> handlingTransaction = transService.findHandlingTransaction();
		return ResponseEntity.ok(handlingTransaction);
	}
	
//	@PutMapping("/transaction/switch-status")
//	public ResponseEntity<TransactionDto> switchStatusToCompleted(@RequestBody Transaction transaction) {
//		TransactionDto transactionDto = transService.updateStatusToCompleted(transaction);
//		
//		return ResponseEntity.ok(transactionDto);
//	}

}
