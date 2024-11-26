package com.interverse.demo.dto;

import java.time.LocalDate;

import com.interverse.demo.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	
	
	private String accountNumber;
	private String password;
	private String email;
	private String nickname;
	
	private String phoneNumber;
	private String country;
	private String city;
	private LocalDate birthday;
	private String gender;
	private String bio;
	
	//勁甫加的
	private Integer id;
    public static UserDto fromEntity(User user) {
    	UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        return dto;
    }

}
