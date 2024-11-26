package com.interverse.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="club_article_photos")
@Getter
@Setter
@NoArgsConstructor
public class ArticlePhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JoinColumn(name = "article_id")
	@ManyToOne
	@JsonIgnoreProperties({"photos", "user","club","comment"})
	private ClubArticle clubArticle;
	
	@Column(name="name")
	private String name;
	
	@Column(name ="url")
	private String url;
	
	private String base64Photo;

	
}
