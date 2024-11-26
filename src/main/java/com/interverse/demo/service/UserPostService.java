package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interverse.demo.model.UserPost;
import com.interverse.demo.model.UserPostRepository;


@Service
public class UserPostService {
	
	@Autowired
	private UserPostRepository userPostRepo;
	
	//C
	public UserPost savePost(UserPost post) {
		return userPostRepo.save(post);
	}
	
	//U
	@Transactional
	public UserPost updatePost(Integer postId, String newcontent) {
		Optional<UserPost> optional = userPostRepo.findById(postId);
	
		if(optional.isPresent()) {
			UserPost userPost = optional.get();
			userPost.setContent(newcontent);
			return userPost;
		}
		return null;
	}

	//R
	public UserPost findPostById(Integer postId) {
		Optional<UserPost> optional = userPostRepo.findById(postId);
	
		if(optional.isPresent()) {
			UserPost userPost = optional.get();
			return userPost;
		}
		return null;
	}
	
	//D
	public void deletePostById(Integer postId) {
		userPostRepo.deleteById(postId);
	}
	
	public List<UserPost> showUserAllPost(Integer userId){
		return userPostRepo.findAllPostByUserId(userId);
	}
	
	
	
}
