package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

	@Query("from PostComment where userPost.id = :id order by added")
	List<PostComment> findAllCommentByPost(@Param("id") Integer postId);
}
