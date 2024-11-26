package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostPhotosRepository extends JpaRepository<PostPhoto, Integer> {

	@Query("from PostPhoto where userPost.id = :id ")
	List<PostPhoto> findPhotoListByPostId(@Param("id") Integer postId);
}
