package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, EventParticipantId> {

    // 查詢單一使用者全部已參加的活動(status=1)
    @Query("SELECT ep.event FROM EventParticipant ep WHERE ep.user.id = :userId AND ep.status = 1")
    List<Event> findApprovedEventsByUserId(@Param("userId") Integer userId);

    // 查詢單一活動status=1的user(正式參與者)
    @Query("SELECT ep FROM EventParticipant ep WHERE ep.event.id = :eventId AND ep.status = 1")
    List<EventParticipant> findApprovedParticipantsByEventId(@Param("eventId") Integer eventId);
    
    // 單一活動查詢status=0的user(供審核)
    @Query("SELECT ep FROM EventParticipant ep WHERE ep.eventParticipantId.eventId = :eventId AND ep.status = 0")
    List<EventParticipant> findPendingParticipantsByEventId(@Param("eventId") Integer eventId);
    
    // 使用者退出某一活動
    @Modifying
    @Transactional
    @Query("DELETE FROM EventParticipant ep WHERE ep.eventParticipantId.userId = :userId AND ep.eventParticipantId.eventId = :eventId")
    void deleteEventFromUser(@Param("userId") Integer userId, @Param("eventId") Integer eventId);
    
    // 單一活動刪除參與者(刪除及審核不通過刪除)
    @Modifying
    @Transactional
    @Query("DELETE FROM EventParticipant ep WHERE ep.eventParticipantId.eventId = :eventId AND ep.eventParticipantId.userId = :userId")
    void deleteUserFromEvent(@Param("eventId") Integer eventId, @Param("userId") Integer userId);
    
    //品琇加的
    @Modifying
    @Transactional
    void deleteByEventIdAndUserId(Integer eventId, Integer userId);
}
