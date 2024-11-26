package com.interverse.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface ProductRepository  extends JpaRepository<Product, Integer>{

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Product p WHERE p.id = :id")
	Optional<Product> findByIdWithLock(@Param("id") Integer id);
	
}
