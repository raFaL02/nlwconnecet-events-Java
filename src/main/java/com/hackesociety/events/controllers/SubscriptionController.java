package com.hackesociety.events.controllers;

import com.hackesociety.events.dto.ErrorMessage;
import com.hackesociety.events.dto.SubscriptionResponse;
import com.hackesociety.events.exception.EventNotFoundException;
import com.hackesociety.events.exception.SubscriptionConflictExcpetion;
import com.hackesociety.events.models.Subscription;
import com.hackesociety.events.models.User;
import com.hackesociety.events.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscription/{prettyName}")
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName, @RequestBody User subscriber) {

        try {
            SubscriptionResponse result = subscriptionService.createNewSubscription(prettyName, subscriber);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (EventNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        } catch (SubscriptionConflictExcpetion ex) {
            return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));
        }

        return ResponseEntity.badRequest().build();
    }
}
