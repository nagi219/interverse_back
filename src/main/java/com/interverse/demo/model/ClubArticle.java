package com.interverse.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "club_articles")
@Getter
@Setter
@NoArgsConstructor
public class ClubArticle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({ "userPosts", "postComment", "sentNotification", "receivedNotification", "transaction",
			"orders", "club", "event", "clubPhoto", "userDetail", "clubArticle", "accountNumber", "password", "email",
			"walletBalance", "added", "clubArticle", "clubArticleComment", "eventPhoto" }) // 忽略不需要序列化的屬性
	private User user;

	@ManyToOne
	@JoinColumn(name = "club_id")
	private Club club;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clubArticle")
	@JsonIgnoreProperties("clubArticle") // 忽略 clubArticle 屬性
	private List<ClubArticleComment> comment;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	private LocalDateTime added;

	@Column(name = "like_count")
	private int likeCount = 0;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clubArticle")
	@JsonIgnoreProperties("clubArticle") // 忽略 clubArticle 屬性
	private List<ArticlePhoto> photos = new ArrayList<>();

	@PrePersist // 當物件要進入persistent狀態前，先執行以下方法
	public void onCreate() {
		if (added == null) {
			added = LocalDateTime.now();
		}
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "article_hashtags", 
				joinColumns = @JoinColumn(name = "article_id"), 
				inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
	private Set<ClubArticleHashtag> hashtags = new HashSet<>();

	public void addHashtag(ClubArticleHashtag hashtag) {
		hashtags.add(hashtag);
	}

	public void removeHashtag(ClubArticleHashtag hashtag) {
		hashtags.remove(hashtag);
	}
}
