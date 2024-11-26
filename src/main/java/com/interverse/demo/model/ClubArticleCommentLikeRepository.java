package com.interverse.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubArticleCommentLikeRepository extends JpaRepository<ClubArticleCommentLike, Integer> {

	boolean existsByUserIdAndCommentId(Integer userId, Integer commentId);
	
	void deleteByUserIdAndCommentId(Integer userId, Integer commentId);
	
	
}
