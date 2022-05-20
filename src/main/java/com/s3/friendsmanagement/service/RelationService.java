package com.s3.friendsmanagement.service;

import com.s3.friendsmanagement.model.User;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;

import java.util.List;
import java.util.Optional;

public interface RelationService {
    User findByEmail(String email);

    Boolean addFriend(CreateFriendConnectionReq createFriendConnectionReq);

    List<String> retrieveFriendsList(EmailRequest emailRequest);

}
