package com.interverse.demo.model;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

	@Column(insertable = false, updatable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	// 來源
	private Integer source;

	private String eventName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime added;
	
	@PrePersist
	protected void onCreate() {
		added = LocalDateTime.now();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubId")
	private Club club;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorId")
	private User eventCreator;	
	
	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "event")
	private EventDetail eventDetail;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "event") 
	private List<EventPhoto> evenPhoto;

}
