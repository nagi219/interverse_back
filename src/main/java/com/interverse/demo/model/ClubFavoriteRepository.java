package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface ClubFavoriteRepository extends JpaRepository<ClubFavorite, ClubFavoriteId> {

	// 用userId找收藏
	List<ClubFavorite> findByClubFavoriteIdUserId(Integer userId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM ClubFavorite cf WHERE cf.clubFavoriteId.clubId = :clubId AND cf.clubFavoriteId.userId = :userId")
	void deleteClubFavoriteFromUser(@Param("userId") Integer userId, @Param("clubId") Integer clubId);

}
