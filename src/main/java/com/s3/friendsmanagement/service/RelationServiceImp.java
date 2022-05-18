package com.s3.friendsmanagement.service;

import com.s3.friendsmanagement.model.User;
import com.s3.friendsmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RelationServiceImp implements RelationService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
