package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubArticlesRepository extends JpaRepository<ClubArticle, Integer> {
	
	@Query("from ClubArticle where club.id = :id order by added desc")
	List<ClubArticle> findAllArticleByClubId(@Param("id") Integer clubId);
	
	//模糊時間
    List<ClubArticle> findByTitleContainingIgnoreCaseAndClubId(String title,Integer clubId);
    
 // 方法 2：使用 @Query 註解
    @Query("SELECT ca FROM ClubArticle ca WHERE LOWER(ca.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<ClubArticle> searchByTitleContaining(@Param("title") String title);

    List<ClubArticle> findByHashtagsTagAndClubId(String tag, Integer clubId);



}
