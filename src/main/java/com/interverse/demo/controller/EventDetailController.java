package com.interverse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.model.Event;
import com.interverse.demo.model.EventDetail;
import com.interverse.demo.service.EventDetailService;
import com.interverse.demo.service.EventService;

@RestController
@RequestMapping("/eventDetail")
public class EventDetailController {
	
	@Autowired
	private EventDetailService edService;
	
	@Autowired
	private EventService eService;
	
	@PostMapping("{id}/new")
	public ResponseEntity<EventDetail> createED(@RequestBody EventDetail eventDetail) {
	    if (eventDetail == null || eventDetail.getEvent() == null || eventDetail.getEvent().getId() == null) {
	        return ResponseEntity.badRequest().body(null); // 返回 400 错误
	    }

	    // 尋找並附加持久化Event實體
	    Event event = eService.findEventById(eventDetail.getEvent().getId());
	    if (event == null) {
	        return ResponseEntity.badRequest().body(null); // 如果 Event 不存在，则返回 400 错误
	    }
	    
	    eventDetail.setEvent(event); // 持久化後設置到 EventDetail

	    // 保存EventDetail實體
	    EventDetail savedEventDetail = edService.saveED(eventDetail);
	    return ResponseEntity.ok(savedEventDetail);
	}
	
	@GetMapping("/{id}/show")
	public ResponseEntity<?> getEventDetailById(@PathVariable Integer id){
		EventDetail result = edService.findEDById(id);
		
		if(result !=null) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.status(HttpStatus.OK).body("無此ID");
	
	}
	
	@PutMapping("/{id}/edit")
	public ResponseEntity<String> updateED(@PathVariable Integer id, @RequestBody EventDetail eventDetail){
		
		EventDetail existED = edService.findEDById(id);
		
		if(existED == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此ID");
		}
		eventDetail.setId(id);
		edService.saveED(eventDetail);
		
		return ResponseEntity.status(HttpStatus.OK).body("Update Successful");
	}
	
	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<String> deleteEventDetail(@PathVariable Integer id){
//		if(edService.findEDById(id)==null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此ID");
//		}
//		edService.deleteEDById(id);
//		
//		return ResponseEntity.status(HttpStatus.OK).body("Delete Successful");
//				
//	}
	
	
	
}
