package com.interverse.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.model.PostPhoto;
import com.interverse.demo.service.PostPhotoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class PostPhotoController {

	@Autowired
	private PostPhotoService photoService;
	
	@PostMapping("/postPhoto")
	//方法的返回類型是 ResponseEntity<PostPhoto>，這是 Spring 的一個類型，允許你設置 HTTP 狀態碼和返回的內容。PostPhoto 是要返回的實體對象類型。
	public ResponseEntity<PostPhoto> createPostPhoto(@RequestParam MultipartFile file,
			@RequestParam("postId") Integer postId) throws IOException {
		
		//完成檔案的儲存工作，並將檔案與指定的 UserPost 關聯。返回的 PostPhoto 實體包含了儲存後的照片信息。
		PostPhoto photo = photoService.createPhoto(file, postId);
		
		//new ResponseEntity<>(photo, HttpStatus.CREATED): 創建一個 ResponseEntity 物件，其中 photo 是要返回的實體對象，HttpStatus.CREATED 是 HTTP 狀態碼 201，表示成功創建了一個新的資源。
		return new ResponseEntity<>(photo, HttpStatus.CREATED);
	}
	
	@GetMapping("/postPhoto/{postId}")
	public List<PostPhoto> getPhotoByPostId(@PathVariable Integer postId) {
		return photoService.findPhotoListByPostId(postId);
	}
	
}
