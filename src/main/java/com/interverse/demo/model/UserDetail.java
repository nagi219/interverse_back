package com.interverse.demo.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Entity @Table(name= "user_details")
public class UserDetail {
	
	@Id
	private Integer userId;
	@Column(unique = true, nullable = false)
	private String phoneNumber;
	private String country;
	@Column(nullable = false)
	private String city;
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	@Column(nullable = false)
	private String gender;
	private String photo;
	private String bio;
	
	@OneToOne @MapsId
	@JoinColumn(name="user_id")
	private User user;
	
}
