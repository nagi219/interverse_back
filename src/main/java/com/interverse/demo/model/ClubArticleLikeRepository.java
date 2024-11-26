package com.interverse.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubArticleLikeRepository extends JpaRepository<ClubArticleLike, Integer>{

    //檢查是否存在userId與ArticleId的匹配實體
	boolean existsByUserIdAndArticleId(Integer userId, Integer articleId);
    
    void deleteByUserIdAndArticleId(Integer userId, Integer articleId);
    
    long countByArticleId(Integer articleId);
}
