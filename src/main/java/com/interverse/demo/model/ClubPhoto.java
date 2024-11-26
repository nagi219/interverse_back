package com.interverse.demo.model;


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

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="clubPhotos")
public class ClubPhoto {
	
	@Column(insertable=false, updatable=false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String photo;
	
	@ManyToOne
	@JoinColumn(name="clubId")
	private Club club;
	
	@ManyToOne
	@JoinColumn(name="uploaderId")
	private User uploaderId;
	

}
