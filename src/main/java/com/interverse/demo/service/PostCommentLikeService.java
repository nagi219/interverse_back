package com.interverse.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.PostComment;
import com.interverse.demo.model.PostCommentLike;
import com.interverse.demo.model.PostCommentLikeRepository;
import com.interverse.demo.model.PostCommentRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostCommentLikeService {

	@Autowired
	private PostCommentLikeRepository commentLikeRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PostCommentRepository commentRepo;

	@Transactional
	public void toggleLike(Integer userId, Integer commentId) {
		Optional<User> userOptional = userRepo.findById(userId);
		Optional<PostComment> commentOptional = commentRepo.findById(commentId);
		if (userOptional.isPresent() && commentOptional.isPresent()) {
			User user = userOptional.get();
			PostComment comment = commentOptional.get();
			boolean liked = commentLikeRepo.existsByUserIdAndPostCommentId(userId, commentId);
			PostCommentLike commentLike = new PostCommentLike();

			if (liked) {
				commentLikeRepo.deleteByUserIdAndPostCommentId(userId, commentId);
				;
				comment.setLikeCount(comment.getLikeCount() - 1);
			} else {
				commentLike.setUser(user);
				commentLike.setPostComment(comment);
				comment.setLikeCount(comment.getLikeCount() + 1);
				commentLikeRepo.save(commentLike);
			}

			commentRepo.save(comment);
		}
	}
	
	public boolean hasUserLikedPostComment(Integer userId, Integer commentId) {
		return commentLikeRepo.existsByUserIdAndPostCommentId(userId, commentId);
	}
}
