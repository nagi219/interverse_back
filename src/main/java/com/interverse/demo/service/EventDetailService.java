package com.interverse.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.EventDetail;
import com.interverse.demo.model.EventDetailRepository;

@Service
public class EventDetailService {

	@Autowired
	private EventDetailRepository edRepo;

	public EventDetail saveED(EventDetail ed) {
		return edRepo.save(ed);
	}

	public EventDetail findEDById(Integer id) {
		Optional<EventDetail> optional = edRepo.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
//	public void deleteEDById(Integer id) {
//		edRepo.deleteById(id);
//	}
	
}
