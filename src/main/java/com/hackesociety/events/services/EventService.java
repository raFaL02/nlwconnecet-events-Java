package com.hackesociety.events.services;

import com.hackesociety.events.models.Event;
import com.hackesociety.events.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event addNewEvent(Event event) {
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    public Event getEventByPrettyName(String prettyName) {
        return eventRepository.findByPrettyName(prettyName);
    }
}
