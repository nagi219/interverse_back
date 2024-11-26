package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.Administrator;
import com.interverse.demo.model.AdministratorRepository;

@Service
public class AdministratorService {
	
	@Autowired
	private AdministratorRepository adminRepo;
	
	@Autowired
	private PasswordEncoder pwdEcoder;
	
	public Administrator register(Administrator admin) {
		
		String encodedPassword = pwdEcoder.encode(admin.getPassword());
		admin.setPassword(encodedPassword);
	
		return adminRepo.save(admin);
	}
	
	public Administrator login(String accountNumber, String password) {
		Administrator admin = adminRepo.findByAccountNumber(accountNumber);
		
		if (admin != null) {
			String encodedPassword = admin.getPassword();
			boolean isMatched = pwdEcoder.matches(password, encodedPassword);
			
			if (isMatched) {
				return admin;
			}
		}
		
		return null;
	}
	
	public Administrator findAdminById(Integer id) {
		
		Optional<Administrator> optional = adminRepo.findById(id);
		
		if (optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	public List<Administrator> findAllAdmin() {
		
		return adminRepo.findAll();
	}
	
	public Administrator updateAdmin(Administrator admin) {
		
		return adminRepo.save(admin);
	}
	
	public void deleteAdminById(Integer id) {
		
		adminRepo.deleteById(id);
	}
	
	
	public boolean exsitsByAccountNumber(String accountNumber) {
		
		Administrator admin = adminRepo.findByAccountNumber(accountNumber);
		return admin != null;
	}

}
