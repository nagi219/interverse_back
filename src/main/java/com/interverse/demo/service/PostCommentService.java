package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interverse.demo.annotation.ActionType;
import com.interverse.demo.annotation.NotifyPostCommentAction;
import com.interverse.demo.model.PostComment;
import com.interverse.demo.model.PostCommentRepository;

@Service
public class PostCommentService {

	@Autowired
	private PostCommentRepository commentRepo;
	
	@NotifyPostCommentAction(action = ActionType.ADD)
	public PostComment addComment(PostComment comment) {
		return commentRepo.save(comment);
	}
	
	public PostComment findCommentById(Integer commentId) {
		Optional<PostComment> optional = commentRepo.findById(commentId);
		if(optional.isPresent()) {
			optional.get();
		}
		return null;
	}
	
	@Transactional
	public PostComment updateComment(Integer commentId, PostComment newComment) {
		Optional<PostComment> optional = commentRepo.findById(commentId);
		if(optional.isPresent()) {
			PostComment comment = optional.get();
			comment.setComment(newComment.getComment());
			return commentRepo.save(comment);
		}
		return null;
	}
	
	public void deleteCommentById(Integer commentId) {
		commentRepo.deleteById(commentId);
	}
	
	public List<PostComment> findCommentByPost(Integer postId){
		List<PostComment> commentList = commentRepo.findAllCommentByPost(postId);
		return commentList;
	}
	
}

	