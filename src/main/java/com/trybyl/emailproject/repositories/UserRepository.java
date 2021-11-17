package com.trybyl.emailproject.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trybyl.emailproject.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
}

