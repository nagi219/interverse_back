package com.interverse.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.model.ArticlePhoto;
import com.interverse.demo.service.ArticlePhotoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class ArticlePhotoController {

	@Autowired
	private ArticlePhotoService photoService;

	@PostMapping("/club/articlePhoto")
	public ResponseEntity<ArticlePhoto> createArticlePhoto(@RequestParam MultipartFile file,
							@RequestParam("articleId") Integer articleId) throws IOException {
		try {
			ArticlePhoto photo = photoService.createArticlePhoto(file, articleId);
			return new ResponseEntity<>(photo, HttpStatus.CREATED);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	


}
