package com.interverse.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.interverse.demo.model.ClubArticle;
import com.interverse.demo.model.ClubArticleComment;

import lombok.Data;

@Data
public class ArticleCommentDTO {

	private Integer id;
	private Integer userId;
	private String userName;
	private String content;
	private LocalDateTime added;
	private int likeCount;
	private String photo;
	private Integer articleId;
	
	
    public static ArticleCommentDTO fromEntity(ClubArticleComment entity) {
    	ArticleCommentDTO dto = new ArticleCommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setAdded(entity.getAdded());
        dto.setLikeCount(entity.getLikeCount());
        dto.setArticleId(entity.getClubArticle().getId());
        
        // 檢查 user 是否為 null
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getNickname());
        }
        
        // 檢查 photos 是否為 null
        if (entity.getPhoto() != null && !entity.getPhoto().isEmpty()) {
            dto.setPhoto(entity.getPhoto());
        }
        
        return dto;
    }
	
}
