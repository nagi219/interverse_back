package com.interverse.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interverse.demo.annotation.ActionType;
import com.interverse.demo.annotation.NotifyPostCommentAction;
import com.interverse.demo.model.Notification;
import com.interverse.demo.model.PostComment;
import com.interverse.demo.model.User;
import com.interverse.demo.service.NotificationService;
import com.interverse.demo.service.UserService;

//@Aspect
//@Component
public class PostCommentActionAspect {
	
	@Autowired
	private NotificationService notifService;
	
	@AfterReturning(pointcut = "@annotation(notifyPostCommentAction)", returning = "result")
    public void notifyPostCommentAction(JoinPoint joinPoint, Object result, NotifyPostCommentAction notifyPostCommentAction) {
        System.out.println("PostCommentActionAspect triggered!");

        if (!(result instanceof PostComment)) {
            return;
        }

        PostComment comment = (PostComment) result;
        User commenter = comment.getUser();
        User postOwner = comment.getUserPost().getUser();

        // 避免自己評論自己的貼文時發送通知
        if (commenter.getId().equals(postOwner.getId())) {
            return;
        }

        ActionType actionType = notifyPostCommentAction.action();
        String message = createNotificationMessage(commenter, actionType);

        Notification notification = new Notification();
        notification.setSource(2); // 假設2代表評論相關的通知
        notification.setContent(message);
        notification.setStatus(false);
        notification.setSender(commenter);
        notification.setReceiver(postOwner);

        try {
            notifService.addNotification(notification);
            System.out.println("Notification for comment action saved.");
        } catch (Exception e) {
            System.err.println("Error saving notification for comment action: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String createNotificationMessage(User commenter, ActionType actionType) {
        String action = actionType == ActionType.ADD ? "添加了新評論" : "更新了評論";
        return "@" + commenter.getAccountNumber() + " 在你的貼文中" + action;
    }

}
