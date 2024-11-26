package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interverse.demo.dto.EventDTO;
import com.interverse.demo.dto.EventParticipantDTO;
import com.interverse.demo.model.Club;
import com.interverse.demo.model.Event;
import com.interverse.demo.model.EventParticipant;
import com.interverse.demo.model.EventParticipantId;
import com.interverse.demo.model.EventRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;
import com.interverse.demo.service.ClubService;
import com.interverse.demo.service.EventParticipantService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventParticipant")
public class EventParticipantController {

    @Autowired
    private EventParticipantService epService;

    @Autowired
    private EventRepository eRepo;

    @Autowired
    private UserRepository uRepo;
    
    @Autowired
    private ClubService cService;

    // 單一成員參加活動(user新增event; status預設0)
    @PostMapping
    public ResponseEntity<?> joinEvent(@RequestBody EventParticipantDTO eventParticipantDTO) {
        EventParticipant newParticipant = convertToEntity(eventParticipantDTO);
        EventParticipant savedParticipant = epService.saveEventParticipant(newParticipant);
        if (savedParticipant != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedParticipant));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to submit participation request.");
    }

    // 查詢單一user的全部已參加的活動(status=1)
    @GetMapping("/user/{userId}/events")
    public ResponseEntity<?> findEventsByUserId(@PathVariable Integer userId) {
        List<Event> events = epService.findApprovedEventsByUserId(userId);
        List<EventDTO> eventDTOs = events.stream().map(this::convertToDTO).collect(Collectors.toList());
        if (eventDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.ok(eventDTOs);
    }

    // 查詢單一活動的全部已參加user(status=1)
    @GetMapping("/event/{eventId}/approved-participants")
    public ResponseEntity<?> listApprovedParticipants(@PathVariable Integer eventId) {
        List<EventParticipant> approvedParticipants = epService.findApprovedParticipantsByEventId(eventId);
        List<EventParticipantDTO> eventParticipantDTOs = approvedParticipants.stream().map(this::convertToDTO).collect(Collectors.toList());
        if (eventParticipantDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.ok(eventParticipantDTOs);
    }

    // 查詢單一活動的待審核user(status=0)
    @GetMapping("/event/{eventId}/pending-participants")
    public ResponseEntity<?> listPendingParticipants(@PathVariable Integer eventId) {
        List<EventParticipant> pendingParticipants = epService.findPendingParticipantsByEventId(eventId);
        List<EventParticipantDTO> participantDTOs = pendingParticipants.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(participantDTOs);
    }

    // 審核通過方法(status=0更新為1)
    @PutMapping("/approve/{eventId}/{userId}")
    public ResponseEntity<String> approveParticipant(@PathVariable Integer eventId, @PathVariable Integer userId) {
        boolean approved = epService.approveParticipant(eventId, userId);
        if (approved) {
            return ResponseEntity.ok("審核通過");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無該參與者或該參與者已參加活動");
        }
    }

    // 從活動中刪除指定用戶
    @DeleteMapping("/event/{eventId}/user/{userId}")
    public ResponseEntity<?> removeParticipant(@PathVariable Integer eventId, @PathVariable Integer userId) {
        try {
            epService.deleteUserFromEvent(eventId, userId);
            return ResponseEntity.ok("參與者已成功從活動中刪除");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // 品琇加的
    @DeleteMapping("/{eventId}/{userId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable Integer eventId, @PathVariable Integer userId) {
    	epService.removeParticipant(eventId, userId);
        return ResponseEntity.ok("參與者已成功從活動中刪除");
    }
    
    // 新增方法: 檢查用戶參與狀態
    @GetMapping("/event/{eventId}/user/{userId}/status")
    public ResponseEntity<?> checkParticipationStatus(@PathVariable Integer eventId, @PathVariable Integer userId) {
        EventParticipantDTO status = epService.checkParticipationStatus(eventId, userId);
        return ResponseEntity.ok(status);
    }

    // 將EventParticipantDTO轉為entity實體
    private EventParticipant convertToEntity(EventParticipantDTO dto) {
        EventParticipant participant = new EventParticipant();
        EventParticipantId participantId = new EventParticipantId(dto.getEventId(), dto.getUserId()); // 創建複合主鍵

        Event event = eRepo.findById(dto.getEventId()).orElse(null);
        User user = uRepo.findById(dto.getUserId()).orElse(null);

        if (event == null || user == null) {
            throw new IllegalStateException("Event or User not found");
        }
        participant.setEventParticipantId(participantId); // 設置複合主鍵
        participant.setEvent(event);
        participant.setUser(user);
        participant.setStatus(0); // 預設為0
        return participant;
    }

    // 將EventParticipant轉為DTO
    private EventParticipantDTO convertToDTO(EventParticipant participant) {
        EventParticipantDTO dto = new EventParticipantDTO();
        dto.setUserId(participant.getUser().getId());
        dto.setEventId(participant.getEvent().getId());
        dto.setStatus(participant.getStatus());
        dto.setUserName(participant.getUser().getNickname());
        return dto;
    }

    // 將Event轉為EventDTO
    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setEventName(event.getEventName());
        
     // 處理 Club 相關信息
	    if (event.getClub() != null) {
	        dto.setClubId(event.getClub().getId());
	        try {
	            Club club = cService.findClubById(event.getClub().getId());
	            if (club != null) {
	                dto.setClubName(club.getClubName());
	            }
	        } catch (Exception e) {
	            // 記錄錯誤，但不中斷處理
	            System.err.println("Error fetching club: " + e.getMessage());
	        }
	    } else {
	        // 當 Club 為 null 時，設置 clubId 為 null，並可能設置一個默認的 clubName
	        dto.setClubId(null);
	        dto.setClubName(null);
	    }
        return dto;
    }
}
