package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticlePhotoRepository extends JpaRepository<ArticlePhoto, Integer>{

	@Query("from ArticlePhoto where clubArticle.id =:id ")
	List<ArticlePhoto> findPhotoListByArticleId(@Param("id") Integer articleId);
}
