package com.interverse.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.dto.EventParticipantDTO;
import com.interverse.demo.model.Event;
import com.interverse.demo.model.EventParticipant;
import com.interverse.demo.model.EventParticipantId;
import com.interverse.demo.model.EventParticipantRepository;
import jakarta.transaction.Transactional;

@Service
public class EventParticipantService {

    @Autowired
    private EventParticipantRepository epRepo;

    @Transactional
    public EventParticipant saveEventParticipant(EventParticipant eventParticipant) {
        EventParticipantId participantId = eventParticipant.getEventParticipantId();
        if (epRepo.existsById(participantId)) {
            throw new IllegalStateException("Participation already exists.");
        }
        return epRepo.save(eventParticipant);
    }

    // 查詢單一使用者全部參加的活動(status=1)
    public List<Event> findApprovedEventsByUserId(Integer userId) {
        return epRepo.findApprovedEventsByUserId(userId);
    }

    // 查詢單一活動status=1的user(正式參與者)
    public List<EventParticipant> findApprovedParticipantsByEventId(Integer eventId) {
        return epRepo.findApprovedParticipantsByEventId(eventId);
    }

    // 單一活動查詢status=0的user(供審核)
    public List<EventParticipant> findPendingParticipantsByEventId(Integer eventId) {
        return epRepo.findPendingParticipantsByEventId(eventId);
    }

    // 使用者退出某一活動
    public void deleteEventFromUser(Integer userId, Integer eventId) {
        epRepo.deleteEventFromUser(userId, eventId);
    }

    // 審核通過參與者()status改為1
    @Transactional
    public boolean approveParticipant(Integer eventId, Integer userId) {
        EventParticipantId id = new EventParticipantId(eventId, userId);
        EventParticipant eventParticipant = epRepo.findById(id).orElse(null);
        if (eventParticipant != null && eventParticipant.getStatus() == 0) {
            eventParticipant.setStatus(1); // status改為1
            epRepo.save(eventParticipant);
            return true;
        }
        return false; // 如果參與者不存在或狀態不是待審核
    }

    // 活動審核不通過或刪除某參與者
    @Transactional
    public void deleteUserFromEvent(Integer eventId, Integer userId) {
        if (!epRepo.existsById(new EventParticipantId(eventId, userId))) {
            throw new IllegalArgumentException("EventParticipant not found with userId " + userId + " and eventId " + eventId);
        }
        epRepo.deleteUserFromEvent(eventId, userId);
    }
    
    //品琇加的
    public void removeParticipant(Integer eventId, Integer userId) {
    	epRepo.deleteByEventIdAndUserId(eventId, userId);
    }
    
    public EventParticipantDTO checkParticipationStatus(Integer eventId, Integer userId) {
        EventParticipantId id = new EventParticipantId(eventId, userId);
        EventParticipant ep = epRepo.findById(id).orElse(null);
        
        EventParticipantDTO dto = new EventParticipantDTO();
        dto.setEventId(eventId);
        dto.setUserId(userId);
        
        if (ep == null) {
            dto.setStatus(-1); // 表示用戶尚未參加
        } else {
            dto.setStatus(ep.getStatus());
        }
        
        return dto;
    }
}
