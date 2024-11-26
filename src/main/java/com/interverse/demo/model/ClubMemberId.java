package com.interverse.demo.model;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.stereotype.Service;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ClubMemberId implements Serializable{
	
	private Integer userId;
	
	private Integer clubId;
	
	public ClubMemberId(Integer clubId, Integer userId) {
		this.clubId = clubId;
		this.userId = userId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(clubId, userId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClubMemberId other = (ClubMemberId) obj;
		return Objects.equals(clubId, other.clubId) && Objects.equals(userId, other.userId);
	}
}
