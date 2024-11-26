package com.interverse.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EventPhotoDTO {
	
	private Integer id;

	private String photo;

	private Integer eventId;
	
	private Integer uploaderId;
	
	private String UserName;
}
