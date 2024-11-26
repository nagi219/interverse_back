package com.interverse.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubMemberDTO {

    private Integer userId;
    
    private Integer clubId;
    
    private Integer status;
    
    private LocalDateTime added;
    
    private String userName;
}
