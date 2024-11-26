package com.interverse.demo.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class ClubFavoriteId implements Serializable{
	
	private Integer clubId;
	private Integer userId;
	
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
		ClubFavoriteId other = (ClubFavoriteId) obj;
		return Objects.equals(clubId, other.clubId) && Objects.equals(userId, other.userId);
	}
	
	public ClubFavoriteId(Integer clubId, Integer userId) {
	    this.clubId = clubId;
	    this.userId = userId;
	}
	

}
