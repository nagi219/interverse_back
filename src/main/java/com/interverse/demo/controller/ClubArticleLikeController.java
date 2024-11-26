package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.service.ClubArticleLikeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/articleLike")
public class ClubArticleLikeController {

	@Autowired
	private ClubArticleLikeService articleLikeService;
	
	@PostMapping
	public ResponseEntity<String> tollgleLike(@RequestParam Integer userId,
												@RequestParam Integer articleId,
												@RequestParam Integer type) {
		articleLikeService.toggleLike(userId, articleId);
		
		return ResponseEntity.ok("Like toggled successfully");
	}
	
	@GetMapping
	public ResponseEntity<Boolean> getLikeStatus(@RequestParam Integer userId,
    											@RequestParam Integer articleId) {
		boolean hasLiked = articleLikeService.hasUserLikedArticle(userId, articleId);
		return ResponseEntity.ok(hasLiked);
	}
	
	
}
