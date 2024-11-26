package com.interverse.demo.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class EventParticipantId implements Serializable{
	
	private Integer eventId;
	private Integer userId;
	
	public EventParticipantId(Integer eventId, Integer userId) {
		this.eventId = eventId;
		this.userId = userId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(eventId, userId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventParticipantId other = (EventParticipantId) obj;
		return Objects.equals(eventId, other.eventId) && Objects.equals(userId, other.userId);
	}
}
