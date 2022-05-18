package com.s3.friendsmanagement.service;

import com.s3.friendsmanagement.model.User;

import java.util.List;
import java.util.Set;

public interface RelationService {

    User findByEmail(String email);

}
