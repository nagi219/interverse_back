package com.interverse.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.Club;
import com.interverse.demo.model.ClubMember;
import com.interverse.demo.model.ClubMemberId;
import com.interverse.demo.model.ClubMemberRepository;
import jakarta.transaction.Transactional;

@Service
public class ClubMemberService {

	@Autowired
	private ClubMemberRepository cmRepo;

	@Transactional
	public ClubMember saveClubMember(ClubMember clubMember) {
		 ClubMemberId memberId = clubMember.getClubMemberId();
	        if (cmRepo.existsById(memberId)) {
	            throw new IllegalStateException("Membership already exists.");
	        }
		return cmRepo.save(clubMember);
	}

	// 查詢單一使用者全部社團(status=1)
	public List<Club> findClubsByUserId(Integer userId) {
		return cmRepo.findApprovedClubsByUserId(userId);
	}

	// 查詢單一社團status=1的user(正式成員)
	public List<ClubMember> findApprovedMembersByClubId(Integer clubId) {
		return cmRepo.findApprovedMembersByClubId(clubId);
	}

	// 單一社團查詢status=0的user(供審核)
	public List<ClubMember> getPendingMembers(Integer clubId) {
		return cmRepo.findPendingMembersByClubId(clubId);
	}

	// 使用者退出某一社團
	public void deleteClubFromUser(Integer userId, Integer clubId) {
		cmRepo.deleteClubFromUser(userId, clubId);
	}

	// 審核通過成員()status改為1
	@Transactional
	public boolean approveMember(Integer clubId, Integer userId) {
		ClubMemberId id = new ClubMemberId(clubId, userId);
		ClubMember clubMember = cmRepo.findById(id).orElse(null);
		if (clubMember != null && clubMember.getStatus() == 0) {
			clubMember.setStatus(1); // status改為1
			cmRepo.save(clubMember);
			return true;
		}
		return false; // 如果成員不存在或狀態不是待審核
	}

	// 社團審核不通過或刪除某成員
	@Transactional
	public void deleteUserFromClub(Integer clubId, Integer userId) {
		if (!cmRepo.existsById(new ClubMemberId(clubId, userId))) {
			throw new IllegalArgumentException("ClubMember not found with userId " + userId + " and clubId " + clubId);
		}
		cmRepo.deleteUserFromClub(clubId, userId);
	}
}
