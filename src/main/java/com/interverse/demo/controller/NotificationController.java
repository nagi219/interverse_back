package com.interverse.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.NotificationDto;
import com.interverse.demo.model.Notification;
import com.interverse.demo.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
	
	@Autowired
	private NotificationService notifService;
	
	@PostMapping("/add")
	public ResponseEntity<NotificationDto> addNotification(@RequestBody Notification notification){
		
		NotificationDto notificationDto = notifService.addNotification(notification);
		return ResponseEntity.status(HttpStatus.CREATED).body(notificationDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<NotificationDto>> getMyNotification(@PathVariable Integer id) {
		
		List<NotificationDto> myNotificationList = notifService.findMyNotificationDto(id);
		return ResponseEntity.ok(myNotificationList);
	}
	
	@GetMapping("/count/{id}")
	public ResponseEntity<Integer> countMyUnreadNotification(@PathVariable Integer id) {
		Integer count = notifService.countMyUnreadNotification(id);
		return ResponseEntity.ok(count);
	}
	
	@PutMapping("/update-status/{id}")
	public ResponseEntity<NotificationDto> updateNotificationStatusById(@PathVariable Integer id) {
		
		Notification notification = notifService.findNotificationById(id);
		
		NotificationDto notificationDto = notifService.updateNotificationStatus(notification);
		return ResponseEntity.ok(notificationDto);
	}
	
	@PutMapping("/update/{userId}/all")
	public ResponseEntity<List<NotificationDto>> updateAllNotificationStatus(@PathVariable Integer userId) {
		
		List<Notification> allNotifcation = notifService.findMyNotification(userId);
		List<NotificationDto> updatedNotifications = new ArrayList<>();
		
		for(Notification notif : allNotifcation) {
			NotificationDto updatedDto = notifService.updateNotificationStatus(notif);
			updatedNotifications.add(updatedDto); 
		}
		
		return ResponseEntity.ok(updatedNotifications);
	}
}
