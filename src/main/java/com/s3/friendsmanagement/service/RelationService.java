package com.s3.friendsmanagement.service;

import com.s3.friendsmanagement.model.User;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RelationService {
    User findByEmail(String email);

    Boolean addFriend(CreateFriendConnectionReq createFriendConnectionReq);

    List<String> retrieveFriendsList(EmailRequest emailRequest);

    List<String> getCommonFriends(CreateFriendConnectionReq friendRequest);

    Boolean subscribeTo(SubscribeAndBlockRequest subscribeRequest);

    Boolean blockEmail(SubscribeAndBlockRequest subscribeRequest);

    Set<String> retrieveEmails(RetrieveRequest retrieveRequest);
}
