package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {
	
	Friend findByUser1IdAndUser2Id(Integer User1Id, Integer User2Id);
	List<Friend> findByUser1Id(Integer User1Id);
	
	@Query("SELECT f FROM Friend f WHERE f.user2.id = :user2Id AND f.status = false")
	List<Friend> findByUser2IdAndStatusFalse(@Param("user2Id") Integer User2Id);

}
