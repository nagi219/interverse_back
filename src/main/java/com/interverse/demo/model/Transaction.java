package com.interverse.demo.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity @Table(name="transactions")
public class Transaction {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String transactionNo;
	private String paymentMethod;
	private Long amount;
	private Short status;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
	private LocalDateTime added;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@PrePersist
    public void onCreate() {
        if (added == null) {
            added = LocalDateTime.now(); // 設置當前的日期時間
        }
    }

}
