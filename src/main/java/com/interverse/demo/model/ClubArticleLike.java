package com.interverse.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "club_article_likes", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id, article_id"}, name="FK_userLikes")
})
@Data
public class ClubArticleLike {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="article_id")
	private ClubArticle article;
	
	@Column(name="type")
	private Integer type;
	
    @Column(name = "added", nullable = false, updatable = false)
	private LocalDateTime added;
	
    @PrePersist
    protected void onCreate() {
        added = LocalDateTime.now();
    }
}
