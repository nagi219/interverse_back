package com.interverse.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.dto.UserDto;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserDetail;
import com.interverse.demo.service.UserService;
import com.interverse.demo.util.AgeCalculator;
import com.interverse.demo.util.JwtUtil;
import com.interverse.demo.util.NullOrEmptyUtil;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AgeCalculator ageCalculator;

	@Autowired
	private NullOrEmptyUtil noeUtil;

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public String register(@RequestBody UserDto userDto) throws JSONException {
		JSONObject responseJson = new JSONObject();

		// 先檢查是否有輸入值
		if (noeUtil.determineString(userDto.getAccountNumber()) || noeUtil.determineString(userDto.getPassword())
				|| noeUtil.determineString(userDto.getEmail()) || noeUtil.determineString(userDto.getNickname())
				|| noeUtil.determineString(userDto.getPhoneNumber()) || noeUtil.determineString(userDto.getCountry())
				|| noeUtil.determineString(userDto.getCity()) || noeUtil.determineLocalDate(userDto.getBirthday())
				|| noeUtil.determineString(userDto.getGender())) {

			responseJson.put("success", false);
			responseJson.put("message", "請輸入必填欄位");

			return responseJson.toString();
		}

		// 有值，封裝資料
		User user = new User();
		UserDetail userDetail = new UserDetail();

		user.setAccountNumber(userDto.getAccountNumber());
		user.setPassword(userDto.getPassword());
		user.setEmail(userDto.getEmail());
		user.setNickname(userDto.getNickname());

		userDetail.setPhoneNumber(userDto.getPhoneNumber());
		userDetail.setCountry(userDto.getCountry());
		userDetail.setCity(userDto.getCity());
		userDetail.setBirthday(userDto.getBirthday());
		userDetail.setGender(userDto.getGender());

		user.setUserDetail(userDetail);

		List<String> errorMessages = new ArrayList<>();

		try {
			// 檢查unique欄位有沒有違反unique約束
			if (userService.existsByAccountNumber(userDto.getAccountNumber())) {
				errorMessages.add("您輸入的帳號已被註冊");
			}
			if (userService.existsByEmail(userDto.getEmail())) {
				errorMessages.add("您輸入的email已被使用");
			}
			if (userService.existsByPhoneNumber(userDto.getPhoneNumber())) {
				errorMessages.add("您輸入的電話已被使用");
			}
			if (!errorMessages.isEmpty()) {
				responseJson.put("success", false);
				responseJson.put("messages", new JSONArray(errorMessages));
				return responseJson.toString();
			}
			// 註冊
			userService.register(user);

			responseJson.put("success", true);
			responseJson.put("message", "註冊成功");

		} catch (Exception e) {

			responseJson.put("success", false);
			responseJson.put("message", "註冊失敗");
			e.printStackTrace();
		}

		return responseJson.toString();
	}

	@PostMapping("/login")
	public String login(@RequestBody String userJson) throws JSONException {

		JSONObject responseJson = new JSONObject();

		JSONObject userObj = new JSONObject(userJson);
		String accountNumber = userObj.isNull("accountNumber") ? null : userObj.getString("accountNumber");
		String password = userObj.isNull("password") ? null : userObj.getString("password");

		// 先檢查是否有輸入值
		if (accountNumber == null || accountNumber.length() == 0 || password == null || password.length() == 0) {
			responseJson.put("success", false);
			responseJson.put("message", "請輸入帳號與密碼");
			return responseJson.toString();
		}

		// 上面有輸入值的話，執行登入邏輯
		User user = userService.login(accountNumber, password);

		// 判斷登入結果
		if (user == null) {
			responseJson.put("success", false);
			responseJson.put("message", "您輸入的帳號或密碼錯誤");
		} else {
			responseJson.put("success", true);
			responseJson.put("message", "登入成功");

			JSONObject loggedInUser = new JSONObject()
					.put("id", user.getId())
					.put("accountNumber", user.getAccountNumber())
					.put("nickname", user.getNickname());

			String token = jwtUtil.generateEncryptedJwt(loggedInUser.toString());

			responseJson.put("token", token);
			responseJson.put("id", user.getId());
			responseJson.put("nickname", user.getNickname());
		}

		return responseJson.toString();

	}

	@DeleteMapping("/secure/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable Integer id) {
		userService.deleteUserById(id);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/secure/{id}")
	public String updateUserById(@PathVariable Integer id, @RequestBody UserDto userDto) throws JSONException {

		User user = userService.findUserById(id);
		UserDetail userDetail = user.getUserDetail();

		JSONObject responseJson = new JSONObject();

		// 先檢查是否有輸入值
		if (noeUtil.determineString(userDto.getEmail())
				|| noeUtil.determineString(userDto.getNickname()) || noeUtil.determineString(userDto.getPhoneNumber())
				|| noeUtil.determineString(userDto.getCountry()) || noeUtil.determineString(userDto.getCity())
				|| noeUtil.determineLocalDate(userDto.getBirthday()) || noeUtil.determineString(userDto.getGender())) {

			responseJson.put("success", false);
			responseJson.put("message", "請輸入必填欄位");

			return responseJson.toString();
		}

		user.setId(id);
		user.setEmail(userDto.getEmail());
		user.setNickname(userDto.getNickname());

		userDetail.setPhoneNumber(userDto.getPhoneNumber());
		userDetail.setCountry(userDto.getCountry());
		userDetail.setCity(userDto.getCity());
		userDetail.setBirthday(userDto.getBirthday());
		userDetail.setGender(userDto.getGender());
		userDetail.setBio(userDto.getBio());

		user.setUserDetail(userDetail);

		List<String> errorMessages = new ArrayList<>();

		try {
			// 檢查unique欄位有沒有違反unique約束
			if (userService.existsByAccountNumber(id, userDto.getAccountNumber())) {
				errorMessages.add("您輸入的帳號已被註冊; ");
			}
			if (userService.existsByEmail(id, userDto.getEmail())) {
				errorMessages.add("您輸入的email已被使用");
			}
			if (userService.existsByPhoneNumber(id, userDto.getPhoneNumber())) {
				System.out.println("error這邊的"+id);
				errorMessages.add("您輸入的電話已被使用");
			}
			if (!errorMessages.isEmpty()) {
				responseJson.put("success", false);
				responseJson.put("messages", new JSONArray(errorMessages));
				return responseJson.toString();
			}
			// 更新會員資料
			userService.updateUserDetail(user);

			responseJson.put("success", true);
			responseJson.put("message", "更新成功");

		} catch (Exception e) {

			responseJson.put("success", false);
			responseJson.put("message", "更新失敗");
		}

		return responseJson.toString();
	}

	@GetMapping("/secure/{id}")
	public ResponseEntity<String> getUserById(@PathVariable Integer id) throws JSONException {

		User user = userService.findUserById(id);

		if (user != null) {
			UserDetail userDetail = user.getUserDetail();
			Integer age = ageCalculator.calculateAge(userDetail.getBirthday());

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);

			JSONObject responseJson = new JSONObject();		

			responseJson.put("id", user.getId());
			responseJson.put("accountNumber", user.getAccountNumber());
			responseJson.put("email", user.getEmail());
			responseJson.put("nickname", user.getNickname());
			responseJson.put("phoneNumber", userDetail.getPhoneNumber());
			responseJson.put("country", userDetail.getCountry());
			responseJson.put("city", userDetail.getCity());
			responseJson.put("birthday", userDetail.getBirthday());
			responseJson.put("age", age);
			responseJson.put("gender", userDetail.getGender());
			responseJson.put("bio", userDetail.getBio());
			responseJson.put("walletBalance",user.getWalletBalance());

			return new ResponseEntity<String>(responseJson.toString(), httpHeaders, HttpStatus.OK);
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping("/secure/profile-photo/{id}")
	public ResponseEntity<String> uploadProfilePhoto(@PathVariable Integer id, @RequestParam MultipartFile file)
			throws IOException {

		User user = userService.findUserById(id);

		if (user != null) {

			String photoDir = userService.updatePhoto(id, file).getUserDetail().getPhoto();

			File fileDB = new File(photoDir);
			byte[] photoFile = Files.readAllBytes(fileDB.toPath());

			String base64Photo = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(photoFile);

			return new ResponseEntity<String>(base64Photo, HttpStatus.CREATED);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/secure/profile-photo/{id}")
	public ResponseEntity<String> showProfilePhoto(@PathVariable Integer id) throws IOException {

		User user = userService.findUserById(id);

		if (user != null) {
			UserDetail userDetail = user.getUserDetail();
			String photoDir = userDetail.getPhoto();

			if (photoDir != null && !photoDir.isEmpty()) {
				File file = new File(photoDir);
				if (file.exists()) {
					byte[] photoFile = Files.readAllBytes(file.toPath());
					String base64Photo = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(photoFile);
					return new ResponseEntity<>(base64Photo, HttpStatus.OK);
				}
			}
		}
		// 如果照片不存在或會員沒有大頭照，返回null
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping("/findName/{id}")
	public String findUserName(@PathVariable("id") Integer id) {
	    User user = userService.findUserById(id);
	    return user.getNickname();
	}
}