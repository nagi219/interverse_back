package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.model.ClubArticleCommentLike;
import com.interverse.demo.service.ClubArticleCommentLikeService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/article/comment/like")
public class ClubArticleCommentLikeController {
	
	@Autowired
	private ClubArticleCommentLikeService commentLikeService;
	
	@PostMapping
	public ResponseEntity<String> toggleLike(@RequestParam Integer userId,
											@RequestParam Integer commentId){
		commentLikeService.toggleLike(userId, commentId);
		return ResponseEntity.ok("Like toggled successfully");
	}
	
	@GetMapping
	public ResponseEntity<Boolean> getLikeStatus(@RequestParam Integer userId,@RequestParam Integer commentId){
		boolean hasLiked = commentLikeService.hasUserLikedComment(userId, commentId);
		return ResponseEntity.ok(hasLiked);
	}
	
}
