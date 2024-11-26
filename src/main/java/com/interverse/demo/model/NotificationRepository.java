package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	
	@Query("from Notification where receiver.id = :id ORDER BY added DESC")
	List<Notification> findByReceiverId(@Param("id") Integer userId);
	
	@Query("SELECT COUNT(n) FROM Notification n WHERE n.receiver.id = :id AND n.status = false")
    Integer countUnreadNotificationsByReceiverId(@Param("id") Integer userId);
}
