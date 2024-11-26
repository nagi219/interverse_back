package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.dto.NotificationDto;
import com.interverse.demo.model.Notification;
import com.interverse.demo.model.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notifRepo;

	public NotificationDto convert(Notification notif) {
		NotificationDto notifDto = new NotificationDto();
		notifDto.setId(notif.getId());
		notifDto.setSource(notif.getSource());
		notifDto.setContent(notif.getContent());
		notifDto.setStatus(notif.getStatus());
		notifDto.setSenderId(notif.getSender().getId());
		notifDto.setReceiverId(notif.getReceiver().getId());
		notifDto.setAdded(notif.getAdded());

		return notifDto;
	}

	public NotificationDto addNotification(Notification notification) {

		Notification savedNotification = notifRepo.save(notification);
		return convert(savedNotification);

	}

	public List<NotificationDto> findMyNotificationDto(Integer id) {

		List<Notification> notificationList = notifRepo.findByReceiverId(id);

		List<NotificationDto> notificationDtoList = notificationList.stream().map(this::convert)
				.collect(Collectors.toList());

		return notificationDtoList;
	}
	
	public List<Notification> findMyNotification(Integer id) {

		return notifRepo.findByReceiverId(id);
	}
	
	public Notification findNotificationById(Integer id) {
		
		Optional<Notification> optional = notifRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Notification> findAllNotifcation() {
		return notifRepo.findAll();
	}

	public Integer countMyUnreadNotification(Integer id) {

		return notifRepo.countUnreadNotificationsByReceiverId(id);

	}

	public NotificationDto updateNotificationStatus(Notification notification) {

		notification.setStatus(true);
		Notification savedNotification = notifRepo.save(notification);
		return convert(savedNotification);

	}

}
