package com.interverse.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity @Table(name="users")
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true, nullable = false)
	private String accountNumber;
	@Column(nullable = false) @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;  //避免密碼欄位在用JSON回應時顯示
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String nickname;
	
	private Long walletBalance = 0L;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	private LocalDateTime added;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnoreProperties("user")
	private UserDetail userDetail;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sender")
	private List<Notification> sentNotification;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "receiver")
	private List<Notification> receivedNotification;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Transaction> transaction;
	
	
	//暐欣的
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
	private List<Order> orders;
	
	
	//靖緯的
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clubCreator")
	private List<Club> club;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventCreator")
	private List<Event> event;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "uploaderId") 
	private List<ClubPhoto> clubPhoto;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "uploaderId") 
	private List<EventPhoto> eventPhoto;
	
	//勁甫的
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "postComment"}) // 忽略 user 屬性
    private List<UserPost> userPosts;


	
    @JsonIgnoreProperties({"user", "userPosts"}) // 忽略 user 屬性
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<PostComment> postComment;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<ClubArticle> clubArticle;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<ClubArticleComment> clubArticleComment;

	
	@PrePersist
    public void onCreate() {
        if (added == null) {
            added = LocalDateTime.now(); // 設置當前的日期時間
        }
    }

	
}
