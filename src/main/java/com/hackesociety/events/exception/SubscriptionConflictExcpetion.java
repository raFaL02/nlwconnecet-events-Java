package com.hackesociety.events.exception;

public class SubscriptionConflictExcpetion extends RuntimeException {
    public SubscriptionConflictExcpetion(String msg) {
        super(msg);
    }
}
