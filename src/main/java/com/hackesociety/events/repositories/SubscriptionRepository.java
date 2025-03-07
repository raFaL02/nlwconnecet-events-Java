package com.hackesociety.events.repositories;

import com.hackesociety.events.models.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

}
