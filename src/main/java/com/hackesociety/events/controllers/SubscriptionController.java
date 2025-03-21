package com.hackesociety.events.controllers;

import com.hackesociety.events.dto.ErrorMessage;
import com.hackesociety.events.dto.SubscriptionResponse;
import com.hackesociety.events.exception.EventNotFoundException;
import com.hackesociety.events.exception.SubscriptionConflictExcpetion;
import com.hackesociety.events.exception.UserIndicatorNotFoundException;
import com.hackesociety.events.models.User;
import com.hackesociety.events.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName, @RequestBody User subscriber, @PathVariable(required = false) Integer userId) {

        try {
            SubscriptionResponse result = subscriptionService.createNewSubscription(prettyName, subscriber, userId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (EventNotFoundException | UserIndicatorNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        } catch (SubscriptionConflictExcpetion ex) {
            return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName) {
        try {
            return ResponseEntity.ok(subscriptionService.getCompleteRanking(prettyName).subList(0, 3));
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String prettyName, @PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(subscriptionService.getRankingByUser(prettyName, userId));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
    }
}
