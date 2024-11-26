package com.interverse.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.FriendDto;
import com.interverse.demo.service.FriendService;

@CrossOrigin
@RestController
@RequestMapping("/friend")
public class FriendController {
	
	@Autowired
	private FriendService friendService;
	
	@GetMapping("/switch-status/{user1Id}/{user2Id}")
	public ResponseEntity<Map<String, String>> switchFriendStatus(@PathVariable Integer user1Id, @PathVariable Integer user2Id) {
		String status = friendService.switchFriendStatus(user1Id, user2Id);
		Map<String, String> response = new HashMap<>();
        response.put("status", status);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{user1Id}/list")
	public ResponseEntity<List<FriendDto>> showMyFriend(@PathVariable Integer user1Id) {
		List<FriendDto> myFriendList = friendService.findMyFriend(user1Id);
		return ResponseEntity.ok(myFriendList);
	}

	@GetMapping("/{user1Id}/requests")
	public ResponseEntity<List<FriendDto>> showFriendRequest(@PathVariable Integer user1Id) {
		List<FriendDto> requestList = friendService.findMyFriendRequest(user1Id);
		return ResponseEntity.ok(requestList);
	}
	
	@GetMapping("/decline-request/{user1Id}/{user2Id}")
	public ResponseEntity<Map<String, String>> declineRequest(@PathVariable Integer user1Id, @PathVariable Integer user2Id) {
		String status = friendService.declineRequest(user1Id, user2Id);
		Map<String, String> response = new HashMap<>();
        response.put("status", status);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/friend-status/{user1Id}/{user2Id}")
	public ResponseEntity<Map<String, String>> getFriendStatus(@PathVariable Integer user1Id,
            @PathVariable Integer user2Id) {
		String status = friendService.getFriendStatus(user1Id, user2Id);

        Map<String, String> response = new HashMap<>();
        response.put("status", status);

        return ResponseEntity.ok(response);
		
	}

}
