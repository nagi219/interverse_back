package com.interverse.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.model.PostPhoto;
import com.interverse.demo.model.UserPost;
import com.interverse.demo.service.UserPostService;
import com.interverse.demo.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class UserPostController {

	@Autowired
	private UserPostService postService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/userPost")
	public ResponseEntity<UserPost> addPost(@RequestBody UserPost post) {
		String content = post.getContent().replaceAll("\\r\\n|\\r|\\n", "\n");
		post.setContent(content);
		UserPost savePost = postService.savePost(post);
		return new ResponseEntity<>(savePost, HttpStatus.CREATED);
	}
	
	@GetMapping("/userPost/showUserAllPost/{userId}")
	public ResponseEntity<List<UserPost>> showUserAllPost(@PathVariable Integer userId) throws IOException {
		try {
		List<UserPost> posts = postService.showUserAllPost(userId);
        
		for (UserPost userPost : posts) {
			List<PostPhoto> photos = userPost.getPhotos();
			for (PostPhoto photo : photos) {
				File file=new File(photo.getUrl());
				byte[] photoFile = Files.readAllBytes(file.toPath());
				String base64Photo = "data:image/png;base64," +Base64.getEncoder().encodeToString(photoFile); 
				photo.setBase64Photo(base64Photo);
			}
			
		}
		return ResponseEntity.ok(posts);
		}catch(IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}
	
	@PutMapping("/userPost/{postId}")
	public UserPost updatePost(@PathVariable Integer postId, 
									@RequestParam String content) {
		UserPost post = postService.findPostById(postId);
		post.setContent(content);
		postService.savePost(post);
		return post;
	}
	
	@DeleteMapping("/userPost/{postId}")
	public void deletePost(@PathVariable Integer postId) {
		postService.deletePostById(postId);
	}
	
	//待做  關鍵字查詢貼文
	
	//標註好友  好友動態牆上也顯示貼文
	
}
