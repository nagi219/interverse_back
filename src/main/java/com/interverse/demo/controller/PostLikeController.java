package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.service.PostLikeService;

@RestController
@RequestMapping("/postLike")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<String> toggleLike(
            @RequestParam Integer userId,
            @RequestParam Integer postId,
            @RequestParam Integer type) {
        postLikeService.toggleLike(userId, postId);
        return ResponseEntity.ok("Like toggled successfully");
    }

    @GetMapping
    public ResponseEntity<Boolean> getLikeStatus(
            @RequestParam Integer userId,
            @RequestParam Integer postId) {
        boolean hasLiked = postLikeService.hasUserLikedPost(userId, postId);
        return ResponseEntity.ok(hasLiked);
    }
}
