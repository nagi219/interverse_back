package com.interverse.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.EventDTO;
import com.interverse.demo.model.Club;
import com.interverse.demo.model.Event;
import com.interverse.demo.model.User;
import com.interverse.demo.service.ClubService;
import com.interverse.demo.service.EventService;
import com.interverse.demo.service.UserService;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/events")
public class EventController {

	@Autowired
	private EventService eService;

	@Autowired
	private ClubService cService;

	@Autowired
	private UserService uService;

	// 轉換DTO
	private EventDTO convertToDTO(Event event) {
	    EventDTO dto = new EventDTO();
	    
	    dto.setId(event.getId());
	    dto.setSource(event.getSource());
	    dto.setEventName(event.getEventName());
	    dto.setAdded(event.getAdded());
	    
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
	    
	    // 處理 EventCreator 相關信息
	    if (event.getEventCreator() != null) {
	        dto.setEventCreatorId(event.getEventCreator().getId());
	        try {
	            User user = uService.findUserById(event.getEventCreator().getId());
	            if (user != null) {
	                dto.setCreatorName(user.getNickname());
	            }
	        } catch (Exception e) {
	            // 記錄錯誤，但不中斷處理
	            System.err.println("Error fetching user: " + e.getMessage());
	        }
	    } else {
	        // 當 EventCreator 為 null 時，設置相關字段為 null 或默認值
	        dto.setEventCreatorId(null);
	        dto.setCreatorName("未知創建者");
	    }
	    
	    return dto;
	}

	// 建立社團活動
	@PostMapping("/new/Event")
	public EventDTO createEvent(@RequestBody Event event) {
		event.setSource(1);
		Event result = eService.saveEvent(event);
		return convertToDTO(result);
	}

	// 建立工作坊
	@PostMapping("/new/Workshop")
	public EventDTO createClass(@RequestBody Event event) {
		event.setSource(2);
		Event result = eService.saveEvent(event);
		return convertToDTO(result);
	}

	@GetMapping
	public List<EventDTO> getAllEvent() {
		List<Event> result = eService.findAllEvent();

		return result.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEvent(@PathVariable Integer id) {
		Event result = eService.findEventById(id);

		if (result != null) {
			EventDTO eventDTO = convertToDTO(result);
			return ResponseEntity.ok(eventDTO);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此ID");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEvent(@PathVariable Integer id) {

		if (eService.findEventById(id) == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此ID");
		}

		eService.deleteEventById(id);

		return ResponseEntity.status(HttpStatus.OK).body("Delete Successful");
	}

	@PutMapping("/{id}/edit")
	public ResponseEntity<?> updateEvent(@PathVariable Integer id, @RequestBody Event event) {
		Event existEvent = eService.findEventById(id);

		if (existEvent == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此ID");
		}
		event.setId(id);
		event.setAdded(existEvent.getAdded());

		return ResponseEntity.ok(convertToDTO(eService.saveEvent(event)));
	}
	
	@GetMapping("/creator")
	public ResponseEntity<?> getEventsByCreator(@RequestParam(required = false) Integer creatorId) {
		if (creatorId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("請先登入");
        }
        
        List<Event> events = eService.findEventsByCreatorId(creatorId);
        List<EventDTO> eventDTOs = events.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
	}
}
