package com.interverse.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name="eventPhotos")
public class EventPhoto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String photo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="eventId")
	private Event event;
	
	@ManyToOne
	@JoinColumn(name="uploaderId")
	private User uploaderId;
}
