package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubArticleCommentRepository extends JpaRepository<ClubArticleComment, Integer> {

	@Query("from ClubArticleComment where clubArticle.id = :id order by added asc")
	List<ClubArticleComment> findAllCommentByArticleId(@Param("id") Integer articleId);
}
