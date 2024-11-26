package com.interverse.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "user_post_photos")
@Getter
@Setter
@NoArgsConstructor
public class PostPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JoinColumn(name = "post_id")
	@ManyToOne
	@JsonIgnoreProperties("photos") // 忽略不需要序列化的屬性
	private UserPost userPost;

	@Column(name = "name")
	private String name;

	@Column(name = "url")
	private String url;

	private String base64Photo;

	public void setBase64Photo(String base64Photo) {
		this.base64Photo = base64Photo;
	}

	public String getBase64Photo() {
		return this.base64Photo;
	}
}
