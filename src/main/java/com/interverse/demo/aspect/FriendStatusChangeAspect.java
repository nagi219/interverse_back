package com.interverse.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interverse.demo.dto.NotificationDto;
import com.interverse.demo.model.Friend;
import com.interverse.demo.model.Notification;
import com.interverse.demo.model.User;
import com.interverse.demo.service.FriendService;
import com.interverse.demo.service.NotificationService;
import com.interverse.demo.service.UserService;

@Aspect
@Component
public class FriendStatusChangeAspect {

	@Autowired
	private NotificationService notifService;

	@Autowired
	private UserService userService;

	@AfterReturning("@annotation(com.interverse.demo.annotation.NotifyFriendStatusChange)")
	public void notifyFriendStatusChange(JoinPoint joinPoint) {
		System.out.println("FriendStatusChangeAspect triggered!");
		// 獲取方法的所有參數
		Object[] args = joinPoint.getArgs();
		
		Integer user1Id = (Integer) args[0];
		Integer user2Id = (Integer) args[1];

		User user1 = userService.findUserById(user1Id);
		User user2 = userService.findUserById(user2Id);

		// 獲取方法執行的結果
		FriendService friendService = (FriendService) joinPoint.getTarget();
		// 自己已加對方的可能性
		Friend possibility1 = friendService.getFriendPossibility(user1Id, user2Id);
		// 對方已加自己的可能性
		Friend possibility2 = friendService.getFriendPossibility(user2Id, user1Id);

		// 根據結果決定通知內容
		String message = determineNotificationMessage(possibility1, possibility2);

		// 發送通知
		if (message != null) {

			String accountNumber = user1.getAccountNumber();
			String msgPrefix = "@" + accountNumber;

			Notification notification = new Notification();
			notification.setSource(1); // friend暫定1
			notification.setContent(msgPrefix + message);
			notification.setStatus(false);
			notification.setSender(user1);
			notification.setReceiver(user2);

			try {
				NotificationDto savedNotification = notifService.addNotification(notification);
				System.out.println("Notification saved with ID: " + savedNotification.getId());
			} catch (Exception e) {
				System.err.println("Error saving notification: " + e.getMessage());
				e.printStackTrace();
			}
			
		} else {
			System.out.println("No notification message generated.");
		}

	}

	private String determineNotificationMessage(Friend possibility1, Friend possibility2) {
		// 自已加對方好友(因為這是friend表格操作後的判斷)
		if (possibility1 != null && possibility2 == null) {
			return "對你發送好友邀請。";
			// 自己接受對方加好友的邀請(因為這是friend表格操作後的判斷)
		} else if (possibility1 != null && possibility2 != null) {
			return "接受你的好友邀請。";
		}
		return null;
	}

}
