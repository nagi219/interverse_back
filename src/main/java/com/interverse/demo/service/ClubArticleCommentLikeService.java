package com.interverse.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.ClubArticleComment;
import com.interverse.demo.model.ClubArticleCommentLike;
import com.interverse.demo.model.ClubArticleCommentLikeRepository;
import com.interverse.demo.model.ClubArticleCommentRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ClubArticleCommentLikeService {

	@Autowired
	private ClubArticleCommentLikeRepository commentLikeRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ClubArticleCommentRepository commentRepo;
	
	@Transactional
	public void toggleLike(Integer userId, Integer commentId) {
		Optional<User> userOptional = userRepo.findById(userId);
		Optional<ClubArticleComment> commentOptional = commentRepo.findById(commentId);
		if(userOptional.isPresent() && commentOptional.isPresent()) {
			User user = userOptional.get();
			ClubArticleComment comment = commentOptional.get();
		
			boolean liked = commentLikeRepo.existsByUserIdAndCommentId(userId, commentId);
			ClubArticleCommentLike commentLike = new ClubArticleCommentLike();
			if(liked) {
				commentLikeRepo.deleteByUserIdAndCommentId(userId, commentId);
				comment.setLikeCount(comment.getLikeCount() - 1);
			}else {
				commentLike.setUser(user);
				commentLike.setComment(comment);
				comment.setLikeCount(comment.getLikeCount()+1);
				commentLikeRepo.save(commentLike);
			}
			commentRepo.save(comment);
		}
	}
	public boolean hasUserLikedComment(Integer userId, Integer commentId) {
		return commentLikeRepo.existsByUserIdAndCommentId(userId, commentId);
	}
}
