package com.interverse.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Integer>{
	
	UserDetail findByPhoneNumber(String phoneNumber);
}
