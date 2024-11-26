package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	List<Transaction> findByUserId(Integer userId);
	
	@Query("SELECT t FROM Transaction t WHERE t.status = 2")
	List<Transaction> findTransactionStatus2();
	
//	Transaction findByTransactionNoAndUserId(String transactionNo, Integer userId);
	
	@Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND (t.status = 1 OR t.status = 2)")
    Long sumAmountsByUserId(@Param("userId") Integer userId);
	
    @Query("SELECT t FROM Transaction t WHERE t.transactionNo LIKE :pattern")
    Transaction findByTransactionNoPattern(@Param("pattern") String pattern);

}
