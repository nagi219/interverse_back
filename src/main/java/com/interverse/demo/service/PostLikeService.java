package com.interverse.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.PostLike;
import com.interverse.demo.model.PostLikeRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserPost;
import com.interverse.demo.model.UserPostRepository;
import com.interverse.demo.model.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostLikeService {

	@Autowired
	private PostLikeRepository postLikeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserPostRepository userPostRepository;

	@Transactional
	public void toggleLike(Integer userId, Integer postId) {
		Optional<User> userOptional = userRepository.findById(userId);
		Optional<UserPost> postOptional = userPostRepository.findById(postId);
		if (userOptional.isPresent() && postOptional.isPresent()) {
			User user = userOptional.get();
			UserPost post = postOptional.get();
			// 檢查是否已經存在喜好記錄
			boolean liked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
			PostLike postLike = new PostLike();

			// 如果存在，則刪除喜好記錄
			if (liked) {
				postLikeRepository.deleteByUserIdAndPostId(userId, postId);
				post.setLikeCount(post.getLikeCount() - 1);
			// 如果不存在，則創建新的喜好記錄
			} else {
				postLike.setUser(user);
				postLike.setPost(post);
//            postLike.setType(type);
				post.setLikeCount(post.getLikeCount() + 1);
				postLikeRepository.save(postLike);
			}
			

			userPostRepository.save(post);
		}
	}

	public boolean hasUserLikedPost(Integer userId, Integer postId) {
		return postLikeRepository.existsByUserIdAndPostId(userId, postId);
	}

}