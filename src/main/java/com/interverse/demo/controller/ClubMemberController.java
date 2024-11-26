package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interverse.demo.dto.ClubDTO;
import com.interverse.demo.dto.ClubMemberDTO;
import com.interverse.demo.model.Club;
import com.interverse.demo.model.ClubMember;
import com.interverse.demo.model.ClubMemberId;
import com.interverse.demo.model.ClubRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;
import com.interverse.demo.service.ClubMemberService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clubMember")
public class ClubMemberController {

	@Autowired
	private ClubMemberService cmService;

	@Autowired
	private ClubRepository cRepo;

	@Autowired
	private UserRepository uRepo;

	// 單一成員加入社團(user新增club;status預設0)
	@PostMapping
	public ResponseEntity<?> joinClub(@RequestBody ClubMemberDTO clubMemberDTO) {
		ClubMember newMember = convertToEntity(clubMemberDTO);
		ClubMember savedMember = cmService.saveClubMember(newMember);
		if (savedMember != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedMember));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to submit membership request.");
	}

	// 查詢單一user的全部已加入club(status=1)
	@GetMapping("/user/{userId}/clubs")
	public ResponseEntity<?> findClubsByUserId(@PathVariable Integer userId) {
		List<Club> clubs = cmService.findClubsByUserId(userId);
		List<ClubDTO> clubDTOs = clubs.stream().map(this::convertToDTO).collect(Collectors.toList());
		if (clubDTOs.isEmpty()) {
			return ResponseEntity.ok(null);
		}
		return ResponseEntity.ok(clubDTOs);
	}

	// 查詢單一club的全部已加入user(status=1)
	@GetMapping("/club/{clubId}/approved-members")
	public ResponseEntity<?> listApprovedMembers(@PathVariable Integer clubId) {
		List<ClubMember> approvedMembers = cmService.findApprovedMembersByClubId(clubId);
		List<ClubMemberDTO> clubMemberDTOs = approvedMembers.stream().map(this::convertToDTO).collect(Collectors.toList());
		if (clubMemberDTOs.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(clubMemberDTOs);
	}

	// 查詢單一社團的待審核user(status=0)
	@GetMapping("/club/{clubId}/pending-members")
	public ResponseEntity<?> listPendingMembers(@PathVariable Integer clubId) {
		List<ClubMember> pendingMembers = cmService.getPendingMembers(clubId);
		List<ClubMemberDTO> memberDTOs = pendingMembers.stream().map(this::convertToDTO).collect(Collectors.toList());
		return ResponseEntity.ok(memberDTOs);
	}

	// 審核通過方法(status=0更新為1)
	@PutMapping("/approve/{clubId}/{userId}")
	public ResponseEntity<String> approveMember(@PathVariable Integer clubId, @PathVariable Integer userId) {
		boolean approved = cmService.approveMember(clubId, userId);
		if (approved) {
			return ResponseEntity.ok("審核通過");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無該成員或該成員已是社團成員");
		}
	}

	// 從社團中刪除指定用戶
	@DeleteMapping("/club/{clubId}/user/{userId}")
	public ResponseEntity<?> removeMember(@PathVariable Integer clubId, @PathVariable Integer userId) {
		try {
			cmService.deleteUserFromClub(clubId, userId);
			return ResponseEntity.ok("成員已成功從社團刪除");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	//使用者自己退出社團
	@DeleteMapping("user/{userId}/delete/{clubId}")
	public ResponseEntity<?> quitMyself(@PathVariable Integer clubId, @PathVariable Integer userId){
		cmService.deleteClubFromUser(userId, clubId);
		return ResponseEntity.ok("您已退出社團");
	}
	
	// 將ClubMemberDTO轉為entity實體
	private ClubMember convertToEntity(ClubMemberDTO dto) {
		ClubMember member = new ClubMember();
		ClubMemberId memberId = new ClubMemberId(dto.getClubId(), dto.getUserId()); // 創建複合主鍵

		Club club = cRepo.findById(dto.getClubId()).orElse(null);
		User user = uRepo.findById(dto.getUserId()).orElse(null);

		if (club == null || user == null) {
			throw new IllegalStateException("Club or User not found");
		}
		member.setClubMemberId(memberId); //設置複合主鍵
		member.setClub(club);
	    member.setUser(user);
		member.setStatus(dto.getStatus()); // 預設為0
		return member;
	}

	// 將ClubMember轉為DTO
	private ClubMemberDTO convertToDTO(ClubMember member) {
		ClubMemberDTO dto = new ClubMemberDTO();
		dto.setUserId(member.getUser().getId());
	    dto.setClubId(member.getClub().getId());
	    dto.setStatus(member.getStatus());
	    dto.setAdded(member.getAdded());
	    dto.setUserName(member.getUser().getNickname());
		return dto;
	}

	// 將Club轉為ClubDTO
	private ClubDTO convertToDTO(Club club) {
		ClubDTO dto = new ClubDTO();
		dto.setId(club.getId());
		dto.setClubName(club.getClubName());
		dto.setIsPublic(club.getIsPublic());
		dto.setPhoto(club.getPhoto());
		return dto;
	}
}
