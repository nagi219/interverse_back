package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, ClubMemberId> {

	//查詢單一使用者全部已加入的社團(status=1)
	@Query("SELECT cm.club FROM ClubMember cm WHERE cm.user.id = :userId AND cm.status = 1")
	List<Club> findApprovedClubsByUserId(@Param("userId") Integer userId);

	// 查詢單一社團status=1的user(正式成員)
	@Query("SELECT cm FROM ClubMember cm WHERE cm.club.id = :clubId AND cm.status = 1")
	List<ClubMember> findApprovedMembersByClubId(@Param("clubId") Integer clubId);
	
	// 單一社團查詢status=0的user(供審核)
	@Query("SELECT cm FROM ClubMember cm WHERE cm.clubMemberId.clubId = :clubId AND cm.status = 0")
	List<ClubMember> findPendingMembersByClubId(@Param("clubId") Integer clubId);
	
	//使用者退出某一社團
	@Modifying
	@Transactional
	@Query("DELETE FROM ClubMember cm WHERE cm.clubMemberId.userId = :userId AND cm.clubMemberId.clubId = :clubId")
	void deleteClubFromUser(@Param("userId") Integer userId, @Param("clubId") Integer clubId);
    
	//單一社團刪除成員(刪除及審核不通過刪除)
	@Modifying
	@Transactional
	@Query("DELETE FROM ClubMember cm WHERE cm.clubMemberId.clubId = :clubId AND cm.clubMemberId.userId = :userId")
	void deleteUserFromClub(@Param("clubId") Integer clubId, @Param("userId") Integer userId);

}
