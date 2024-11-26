package com.interverse.demo.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="post_comment_like", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "comment_id"}, name="FL_postCommentLikes")
})
@Getter
@Setter
public class PostCommentLike {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn( name ="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="comment_id")
	private PostComment postComment;
	
    @Column(name = "added", nullable = false, updatable = false)
	private LocalDateTime added;
    
    @PrePersist
    protected void onCreate() {
        added = LocalDateTime.now();
    }

}
