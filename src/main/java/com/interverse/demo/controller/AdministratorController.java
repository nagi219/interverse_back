package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.model.Administrator;
import com.interverse.demo.service.AdministratorService;
import com.interverse.demo.util.JwtUtil;
import com.interverse.demo.util.NullOrEmptyUtil;

@RestController
public class AdministratorController {

	@Autowired
	private AdministratorService adminService;

	@Autowired
	private NullOrEmptyUtil noeUtil;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping("/admin/void")
	public ResponseEntity<String> getVoid() {
		return ResponseEntity.ok("已檢查權限");
	}
	
	@PostMapping("/auth")
	public ResponseEntity<String> getAuth(@RequestBody String auth) throws JSONException {
		
		JSONObject authObj = new JSONObject(auth);
		String authString = authObj.getString("auth");
		
		if("123".equals(authString)) {
			
			//產生JWT
			JSONObject tokenData = new JSONObject()
					.put("id", "InterverseAdmin")
					.put("authority", "InitialEntering");
			
			String token = jwtUtil.generateEncryptedJwt(tokenData.toString());
			
			//回應這些資料，讓前端可以塞到axios headers裡
			JSONObject response = new JSONObject()
					.put("token", token)
					.put("id", "InterverseAdmin");
			
			return ResponseEntity.ok(response.toString());
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("驗證失敗");
		
	}

	@PostMapping("/admin/register")
	public ResponseEntity<String> register(@RequestBody Administrator admin) {

		try {
			if (noeUtil.determineString(admin.getAccountNumber()) || noeUtil.determineString(admin.getPassword())
					|| noeUtil.determineString(admin.getNickname()) || noeUtil.determineString(admin.getAuthority())) {
				return ResponseEntity.badRequest().body("請輸入所有必填欄位");
			}

			if (adminService.exsitsByAccountNumber(admin.getAccountNumber())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("您輸入的帳號已被註冊");
			}

			adminService.register(admin);
			return ResponseEntity.status(HttpStatus.CREATED).body("註冊成功");

		} catch (Exception e) {

			System.err.println("註冊過程中發生錯誤: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("註冊過程中發生錯誤，請稍後再試");
		}
	}

	@PostMapping("/admin/login")
	public ResponseEntity<String> login(@RequestBody String adminJson) throws JSONException {

		JSONObject adminObj = new JSONObject(adminJson);
		if (noeUtil.determineString(adminObj.getString("accountNumber"))
				|| noeUtil.determineString(adminObj.getString("password"))) {
			return ResponseEntity.badRequest().body("請輸入帳號與密碼");
		}
		try {
			Administrator admin = adminService.login(adminObj.getString("accountNumber"),
					adminObj.getString("password"));

			if (admin == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼輸入錯誤");
			}
			
			//產生JWT
			JSONObject loggedInAdmin = new JSONObject()
			.put("id", admin.getId())
			.put("authority", admin.getAuthority());
			
			String token = jwtUtil.generateEncryptedJwt(loggedInAdmin.toString());
			
			//封裝回應內容
			JSONObject responseJson = new JSONObject()
			.put("token", token)
			.put("id", admin.getId())
			.put("nickname", admin.getNickname())
			.put("authority", admin.getAuthority());

			return ResponseEntity.ok(responseJson.toString());

		} catch (Exception e) {
			
			System.err.println("登入過程中發生錯誤: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登入過程中發生錯誤，請稍後再試");
		}
	}

}
