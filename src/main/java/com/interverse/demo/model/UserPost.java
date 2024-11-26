package com.interverse.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity @Table(name="user_posts")
public class UserPost {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String content;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	@Temporal(TemporalType.TIMESTAMP) // 年月日時分秒
	private LocalDateTime added;
	
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"userDetail","sentNotification","receivedNotification","transaction","orders","club","event","clubPhoto","eventPhoto","userPosts","postComment","clubArticle","clubArticleComment"	
    }) // 忽略不需要序列化的屬性
    private User user;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userPost")
    @JsonIgnoreProperties({"user","userPost"}) // 忽略 userPost 屬性	
	private List<PostComment> postComment;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userPost")
    @JsonIgnoreProperties({"userPost"}) // 忽略 userPost 屬性
	private List<PostPhoto> photos;
	
	@Column(name="like_count")
	private int likeCount=0;
	
	@PrePersist // 當物件要進入persistent狀態前，先執行以下方法
	public void onCreate() {
		if (added == null) {
			added =LocalDateTime.now();
		}
	}
	


}
