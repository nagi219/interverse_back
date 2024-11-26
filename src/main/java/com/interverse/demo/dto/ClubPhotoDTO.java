package com.interverse.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubPhotoDTO {
	
	private Integer id;

	private String photo;

	private Integer clubId;

	private Integer uploaderId;
	
	private String UserName;
}
