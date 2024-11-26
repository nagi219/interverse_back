package com.interverse.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.model.PostComment;
import com.interverse.demo.model.UserPost;
import com.interverse.demo.service.PostCommentService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class PostCommentController {

	@Autowired
	private PostCommentService commentService;
	
	@PostMapping("/postComment")
	public PostComment addComment(@RequestBody PostComment postComment) {
		String content = postComment.getComment().replaceAll("\\r\\n|\\r|\\n", "\n");
		postComment.setComment(content);
		return commentService.addComment(postComment);
	}
	
	@PutMapping("/postComment/{commentId}")
	public PostComment updateComment(@PathVariable Integer commentId, @RequestBody PostComment newComment) {
		return commentService.updateComment(commentId, newComment);
	}
	
	@DeleteMapping("/postComment/{commentId}")
	public void deleteComment(@PathVariable Integer commentId) {
		commentService.deleteCommentById(commentId);
	}
	
	@GetMapping("/postComment/{postId}")
	public List<PostComment> showPostComment(@PathVariable Integer postId) {
		return commentService.findCommentByPost(postId);
	}
	
}
