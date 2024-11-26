package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.dto.TransactionDto;
import com.interverse.demo.model.Transaction;
import com.interverse.demo.model.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transRepo;

	public TransactionDto convert(Transaction transaction) {
		TransactionDto transactionDto = new TransactionDto();

		transactionDto.setId(transaction.getId());
		transactionDto.setTransactionNo(transaction.getTransactionNo());
		transactionDto.setPaymentMethod(transaction.getPaymentMethod());
		transactionDto.setAmount(transaction.getAmount());
		transactionDto.setStatus(transaction.getStatus());
		transactionDto.setAdded(transaction.getAdded());
		transactionDto.setUserId(transaction.getUser().getId());

		return transactionDto;
	}

	public TransactionDto addTransaction(Transaction transaction) {

		return convert(transRepo.save(transaction));
	}

	public TransactionDto updateStatusToCompleted(Integer transactionId) {
		
		Optional<Transaction> optional = transRepo.findById(transactionId);
		
		if(optional != null) {
			Transaction transaction = optional.get();
			transaction.setStatus((short) 1);
			return convert(transRepo.save(transaction));
		}
		
		return null;
	}
	
	public TransactionDto updateStatusToFailed(Integer transactionId) {
		
		Optional<Transaction> optional = transRepo.findById(transactionId);
		
		if(optional != null) {
			Transaction transaction = optional.get();
			transaction.setStatus((short) 0);
			return convert(transRepo.save(transaction));
		}
		
		return null;
	}

//	// forUser
//	public TransactionDto updateStatusToCompleted(String transactionNo, Integer userId) {
//		Transaction transaction = transRepo.findByTransactionNoAndUserId(transactionNo, userId);
//
//		if (transaction != null) {
//			transaction.setStatus((short) 1);
//			return convert(transRepo.save(transaction));
//		}
//		return null;
//	}
//	// forUser
//    public TransactionDto updateStatusToFailed(String transactionNo, Integer userId) {
//        Transaction transaction = transRepo.findByTransactionNoAndUserId(transactionNo, userId);
//        
//		if (transaction != null) {
//			transaction.setStatus((short) 0);
//			return convert(transRepo.save(transaction));
//		}
//		return null;
//    }

	public List<TransactionDto> findMyTransaction(Integer userId) {

		List<Transaction> transactionList = transRepo.findByUserId(userId);
		List<TransactionDto> transactionDtoList = transactionList.stream().map(this::convert)
				.collect(Collectors.toList());

		return transactionDtoList;
	}

	public List<TransactionDto> findAllTransaction() {

		List<Transaction> transactionList = transRepo.findAll();
		List<TransactionDto> transactionDtoList = transactionList.stream().map(this::convert)
				.collect(Collectors.toList());

		return transactionDtoList;
	}

	public List<TransactionDto> findHandlingTransaction() {

		List<Transaction> transactionList = transRepo.findTransactionStatus2();
		List<TransactionDto> transactionDtoList = transactionList.stream().map(this::convert)
				.collect(Collectors.toList());

		return transactionDtoList;
	}
	
    public TransactionDto findTransactionsByEventAndUser(Integer eventId, Integer userId) {
        String pattern = String.format("E%05d%%U%05d%%R", eventId, userId);
        
        Transaction transaction = transRepo.findByTransactionNoPattern(pattern);
        
        if(transaction!= null) {
        	return convert(transaction);
        }
        
        return null;
    }

}
