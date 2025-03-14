package com.hackesociety.events.services;

import com.hackesociety.events.dto.SubscriptionResponse;
import com.hackesociety.events.exception.EventNotFoundException;
import com.hackesociety.events.exception.SubscriptionConflictExcpetion;
import com.hackesociety.events.models.Event;
import com.hackesociety.events.models.Subscription;
import com.hackesociety.events.models.User;
import com.hackesociety.events.repositories.EventRepository;
import com.hackesociety.events.repositories.SubscriptionRepository;
import com.hackesociety.events.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    public SubscriptionResponse createNewSubscription(String eventName, User user) {
        //recuperar evento pelo nome
        Event event = eventRepository.findByPrettyName(eventName);
        if (event == null) {
            throw new EventNotFoundException("Evento " + eventName + " não existe!");
        }

        User userRec = userRepository.findByEmail(user.getEmail());
        if (userRec == null) {
            userRec = userRepository.save(user);
        }

        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(userRec);

        Subscription tmpSub = subscriptionRepository.findByEventAndSubscriber(event, userRec);
        if (tmpSub != null) {
            throw new SubscriptionConflictExcpetion("Já existe uma inscrição para o usuário " + userRec.getName() + " no evento " + event.getTitle());
        }

        Subscription res = subscriptionRepository.save(subscription);

        return new SubscriptionResponse(res.getSubscriptionNumber(), "http://javasummer.com/" + res.getEvent().getPrettyName() + "/" + res.getSubscriber().getId());
    }
}
