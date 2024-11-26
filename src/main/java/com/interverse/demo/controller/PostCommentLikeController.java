package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.service.PostCommentLikeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/postCommentLike")
public class PostCommentLikeController {

	@Autowired
	private PostCommentLikeService commentLikeService;
	
	@PostMapping
	public ResponseEntity<String> toggleLike(@RequestParam Integer userId,
											@RequestParam Integer commentId){
		commentLikeService.toggleLike(userId, commentId);
		return ResponseEntity.ok("Like toggle successfully");
	}
	
	@GetMapping
	public ResponseEntity<Boolean> getLikeStatus(
            @RequestParam Integer userId,
            @RequestParam Integer commentId) {
        boolean hasLiked = commentLikeService.hasUserLikedPostComment(userId, commentId);
        return ResponseEntity.ok(hasLiked);
    }
	
}
