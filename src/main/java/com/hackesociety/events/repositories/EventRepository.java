package com.hackesociety.events.repositories;

import com.hackesociety.events.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Integer> {

    public Event findByPrettyName(String prettyName);
}
