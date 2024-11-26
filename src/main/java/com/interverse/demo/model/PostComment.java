package com.interverse.demo.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_comments")
@Getter
@Setter
public class PostComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"userDetail","sentNotification","receivedNotification","transaction","orders","club","event","clubPhoto","eventPhoto","userPosts","postComment","clubArticle","clubArticleComment"	
    })
	private User user;
	
	@Column(name = "comment")
	private String comment;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	private LocalDateTime added;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"postComment","photos"}) // 忽略不需要序列化的屬性
	private UserPost userPost;
	
	@Column(name="like_count")
	private Integer likeCount=0;
	
	@PrePersist // 當物件要進入persistent狀態前，先執行以下方法
	public void onCreate() {
		if (added == null) {
			added =LocalDateTime.now();
		}
	}
	
}
