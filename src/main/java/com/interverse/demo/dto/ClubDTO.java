package com.interverse.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ClubDTO {
	private Integer id;

	private String clubName;

	private String description;

	private Integer isPublic;

	private Integer isAllowed;
	
	private LocalDateTime added;

	private Integer clubCreator;
	
	private String userName;
	
	private String photo;
	
//	private List<String> eventNames; 

}
