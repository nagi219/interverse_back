package com.interverse.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer>{
	
	Administrator findByAccountNumber(String accountNumber);
}
