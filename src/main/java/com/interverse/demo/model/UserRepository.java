package com.interverse.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByAccountNumber(String accountNumber);
	User findByEmail(String email);
}
