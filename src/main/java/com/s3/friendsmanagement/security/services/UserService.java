package com.s3.friendsmanagement.security.services;

import com.s3.friendsmanagement.model.User;

import java.util.Optional;

public interface UserService {
    Iterable<User> findAll();
    Optional<User> findById(long id);
}
