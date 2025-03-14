package com.hackesociety.events.repositories;

import com.hackesociety.events.models.Event;
import com.hackesociety.events.models.Subscription;
import com.hackesociety.events.models.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);
}
