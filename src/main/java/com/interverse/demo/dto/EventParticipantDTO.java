package com.interverse.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EventParticipantDTO {
	
	private Integer eventId;
	
	private Integer userId;
	
	private Integer status;
	
	private String UserName;

}
