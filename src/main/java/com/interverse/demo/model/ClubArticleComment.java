package com.interverse.demo.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "club_article_comments")
@Getter
@Setter
public class ClubArticleComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private ClubArticle clubArticle;

	@Column(name = "content")
	private String content;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	private LocalDateTime added;

	@PrePersist // 當物件要進入persistent狀態前，先執行以下方法
	public void onCreate() {
		if (added == null) {
			added =LocalDateTime.now();
		}
	}
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="like_count")
	private int likeCount=0;

	public ClubArticleComment() {
	}


}
