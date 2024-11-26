package com.interverse.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubFavoriteDTO {

	private Integer clubId;
	private Integer userId;
	private LocalDateTime added;

	public ClubFavoriteDTO(Integer clubId, Integer userId, LocalDateTime added) {
		this.clubId = clubId;
		this.userId = userId;
		this.added = added;
	}
}
