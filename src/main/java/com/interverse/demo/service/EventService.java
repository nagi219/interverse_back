package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.Event;
import com.interverse.demo.model.EventRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eRepo;

	public Event saveEvent(Event eve) {
		return eRepo.save(eve);
	}

	public Event findEventById(Integer id) {
		Optional<Event> optional = eRepo.findById(id);

		if (optional.isPresent()) {

			return optional.get();

		}
		return null;
	}

	public void deleteEventById(Integer id) {
		eRepo.deleteById(id);
	}

	public List<Event> findAllEvent() {
		return eRepo.findAll();
	}

	public boolean existsById(Integer id) {
		return eRepo.existsById(id);
	}
	
	public List<Event> findEventsByCreatorId(Integer creatorId) {
	    return eRepo.findByEventCreatorId(creatorId);
	}
}
