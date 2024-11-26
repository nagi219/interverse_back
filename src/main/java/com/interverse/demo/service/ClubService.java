package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.Club;
import com.interverse.demo.model.ClubMember;
import com.interverse.demo.model.ClubMemberId;
import com.interverse.demo.model.ClubMemberRepository;
import com.interverse.demo.model.ClubRepository;
import com.interverse.demo.model.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ClubService {

	@Autowired
	private ClubRepository clubRepo;

	@Autowired
	private ClubMemberRepository cmRepo;
	
//	@Autowired
//    private UserRepository userRepo; 
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Club saveClub(Club club) {
        Club savedClub = clubRepo.save(club);
        
        // 創建並保存 ClubMember 實例
        ClubMember creatorMember = new ClubMember();
        
        // 設置 ClubMemberId
        ClubMemberId memberId = new ClubMemberId();
        memberId.setClubId(savedClub.getId());
        memberId.setUserId(savedClub.getClubCreator().getId());
        creatorMember.setClubMemberId(memberId);
        
        // 設置關聯
        creatorMember.setClub(savedClub);
        creatorMember.setUser(savedClub.getClubCreator());
        
        // 設置狀態
        creatorMember.setStatus(1); // 直接設置狀態為已批准
        
        // 保存 ClubMember
        cmRepo.save(creatorMember);

        return savedClub;
    }
	

	public Club findClubById(Integer id) {
		Optional<Club> optional = clubRepo.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public void deleteClubById(Integer id) {
		clubRepo.deleteById(id);
	}

	public List<Club> findAllClub() {
		return clubRepo.findAll();
	}
	
	//使用者創建的社團
	public List<Club> findClubsByCreatorId(Integer creatorId) {
		return clubRepo.findByClubCreator_Id(creatorId);
	}
}
