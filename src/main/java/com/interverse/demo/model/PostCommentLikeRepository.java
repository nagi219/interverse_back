package com.interverse.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentLikeRepository  extends JpaRepository<PostCommentLike, Integer>{

	
	boolean existsByUserIdAndPostCommentId(Integer userId, Integer postCommentId);
    
    void deleteByUserIdAndPostCommentId(Integer userId, Integer postCommentId);
    
    long countByPostCommentId(Integer postCommentId);
	
}
