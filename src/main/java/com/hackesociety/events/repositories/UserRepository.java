package com.hackesociety.events.repositories;

import com.hackesociety.events.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
}
