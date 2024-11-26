package com.interverse.demo.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class FriendId implements Serializable {
	
	private Integer user1Id;
	private Integer user2Id;
	
	@Override
	public int hashCode() {
		return Objects.hash(user1Id, user2Id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FriendId other = (FriendId) obj;
		return Objects.equals(user1Id, other.user1Id) && Objects.equals(user2Id, other.user2Id);
	}
	
}
