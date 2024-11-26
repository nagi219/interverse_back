package com.interverse.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.ArticleCommentDTO;
import com.interverse.demo.service.ClubArticlesCommentsService;


@RestController
@RequestMapping("/club/article/comment")
public class ArticleCommentController {

	@Autowired
	private ClubArticlesCommentsService commentService;
	
	@PostMapping
	public ResponseEntity<ArticleCommentDTO> addComment(@RequestBody ArticleCommentDTO articleDTO) {
		String saveContent = articleDTO.getContent().replaceAll("\\r\\n|\\r|\\n", "\n");
		articleDTO.setContent(saveContent);
		ArticleCommentDTO savedComment = commentService.createComment(articleDTO);
		return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
	}
	
	 @PutMapping("/{commentId}")
	    public ResponseEntity<ArticleCommentDTO> updateComment(@PathVariable Integer commentId, @RequestBody ArticleCommentDTO commentDTO) {
	        ArticleCommentDTO updatedComment = commentService.updateCommentById(commentId, commentDTO);
	        if (updatedComment != null) {
	            return ResponseEntity.ok(updatedComment);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @DeleteMapping("/{commentId}")
	    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
	    	commentService.deleteCommentById(commentId);
	        return ResponseEntity.noContent().build();
	    }
	
}
