package com.interverse.demo.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity @Table(name="friends")
public class Friend {
	
	@EmbeddedId
	private FriendId friendId;
	
	private Boolean status;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	private LocalDateTime added;
	
	@ManyToOne
	@MapsId("user1Id")
	@JoinColumn(name="user1_id")
	private User user1;
	
	@ManyToOne
	@MapsId("user2Id")
	@JoinColumn(name="user2_id")
	private User user2;
	
	@PrePersist
	public void onCreate() {
		if (added == null) {
            added = LocalDateTime.now(); // 設置當前的日期時間
        }
	}

}
