package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.annotation.NotifyFriendStatusChange;
import com.interverse.demo.dto.FriendDto;
import com.interverse.demo.model.Friend;
import com.interverse.demo.model.FriendId;
import com.interverse.demo.model.FriendRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepo;

	@Autowired
	private UserRepository userRepo;

	public FriendDto convert(Friend friend) {
		FriendDto friendDto = new FriendDto();
		friendDto.setUser1Id(friend.getUser1().getId());
		friendDto.setUser2Id(friend.getUser2().getId());
		friendDto.setStatus(friend.getStatus());

		return friendDto;
	}
	
	@NotifyFriendStatusChange
	public String switchFriendStatus(Integer user1Id, Integer user2Id) {
		//自己已加對方的可能性
		Friend possibility1 = friendRepo.findByUser1IdAndUser2Id(user1Id, user2Id);
		//對方已加自己的可能性
		Friend possibility2 = friendRepo.findByUser1IdAndUser2Id(user2Id, user1Id);

		Optional<User> optional1 = userRepo.findById(user1Id);
		User user1 = optional1.get();

		Optional<User> optional2 = userRepo.findById(user2Id);
		User user2 = optional2.get();

		if (possibility1 == null && possibility2 == null) {
			// 自已加對方好友

			FriendId friendId = new FriendId();
			friendId.setUser1Id(user1Id);
			friendId.setUser2Id(user2Id);

			Friend friend = new Friend();
			friend.setFriendId(friendId);
			friend.setUser1(user1);
			friend.setUser2(user2);
			friend.setStatus(false);

			friendRepo.save(friend);
			
			return "pending_sent";

		} else if (possibility1 != null && possibility2 == null) {
			// 自己取消對對方的加好友申請

			FriendId friendId = new FriendId();
			friendId.setUser1Id(user1Id);
			friendId.setUser2Id(user2Id);

			friendRepo.deleteById(friendId);
			
			return "not_friend";
			
		} else if (possibility1 == null && possibility2 != null) {
			// 自己接受對方加好友的邀請

			possibility2.setStatus(true);
			friendRepo.save(possibility2);

			FriendId friendId = new FriendId();
			friendId.setUser1Id(user1Id);
			friendId.setUser2Id(user2Id);

			Friend friend = new Friend();
			friend.setFriendId(friendId);
			friend.setUser1(user1);
			friend.setUser2(user2);
			friend.setStatus(true);

			friendRepo.save(friend);
			
			return "friends";
		} else if (possibility1 != null && possibility2 != null) {
			// 刪除與對方的好友關係

			FriendId friendId1 = new FriendId();
			friendId1.setUser1Id(user1Id);
			friendId1.setUser2Id(user2Id);

			friendRepo.deleteById(friendId1);

			FriendId friendId2 = new FriendId();
			friendId2.setUser1Id(user2Id);
			friendId2.setUser2Id(user1Id);

			friendRepo.deleteById(friendId2);
			
			return "not_friend";
		}
		
		 return getFriendStatus(user1Id, user2Id);
	}

	public String declineRequest(Integer user1Id, Integer user2Id) {

		FriendId friendId = new FriendId();
		friendId.setUser1Id(user2Id);
		friendId.setUser2Id(user1Id);

		friendRepo.deleteById(friendId);
		
		return "not_friend";
	}

	public List<FriendDto> findMyFriend(Integer user1Id) {

		List<Friend> friendList = friendRepo.findByUser1Id(user1Id);

		List<FriendDto> friendDtoList = friendList.stream()
				.map(this::convert)
				.collect(Collectors.toList());

		return friendDtoList;
	}

	public List<FriendDto> findMyFriendRequest(Integer user2Id) {

		List<Friend> friendList = friendRepo.findByUser2IdAndStatusFalse(user2Id);

		List<FriendDto> friendDtoList = friendList.stream().map(this::convert).collect(Collectors.toList());

		return friendDtoList;
	}

	
	public String getFriendStatus(Integer user1Id, Integer user2Id) {
         Friend possibility1 = friendRepo.findByUser1IdAndUser2Id(user1Id, user2Id);
         Friend possibility2 = friendRepo.findByUser1IdAndUser2Id(user2Id, user1Id);
         
         if (possibility1 == null && possibility2 == null) {
             return "not_friend";
         } else if ((possibility1 != null && !possibility1.getStatus()) || 
                    (possibility2 != null && !possibility2.getStatus())) {
             return possibility1 != null ? "pending_sent" : "pending_received";
         } else {
             return "friends";
         }
    }
	
	
	// for notification aspect
    public Friend getFriendPossibility(Integer user1Id, Integer user2Id) {
        return friendRepo.findByUser1IdAndUser2Id(user1Id, user2Id);
    }
}
