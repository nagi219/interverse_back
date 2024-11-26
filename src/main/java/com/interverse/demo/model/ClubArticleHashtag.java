package com.interverse.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="club_article_hashtag")
@Data
public class ClubArticleHashtag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="tag", unique = true)
	private String tag;
	
	@ManyToMany(mappedBy = "hashtags")
	@JsonIgnore
	private Set<ClubArticle> articles = new HashSet<>();

	
}
