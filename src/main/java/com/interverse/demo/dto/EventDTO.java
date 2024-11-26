package com.interverse.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EventDTO {
    
	private Integer id;
	
    private Integer source;
    
    private String eventName;
    
    private LocalDateTime added;
    
    private String clubName; // 假设你想返回 club 的名字
   
    private String creatorName; // 假设你想返回 creator 的名字
    
    private Integer clubId;
    
    private EventDetailDTO eventDetail; // 嵌套的 DTO
    
    private Integer eventCreatorId;
    
//    private Integer eventCreator;

}
