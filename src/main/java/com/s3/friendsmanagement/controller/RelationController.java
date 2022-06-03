package com.s3.friendsmanagement.controller;

import com.s3.friendsmanagement.exception.InputInvalidException;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import com.s3.friendsmanagement.payload.response.RetrieveEmailResponse;
import com.s3.friendsmanagement.payload.response.RetrieveFriendsListResponse;
import com.s3.friendsmanagement.payload.response.SuccessResponse;
import com.s3.friendsmanagement.service.RelationService;
import com.s3.friendsmanagement.utils.EmailUtils;
import com.s3.friendsmanagement.utils.ErrorConstraints;
import com.s3.friendsmanagement.utils.RequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/emails")
public class RelationController {

    @Autowired
    private RelationService relationService;

    // create a friend connection between two email addresses
    @PostMapping("/add")
    public ResponseEntity<SuccessResponse> addFriend(
            @Valid @RequestBody CreateFriendConnectionReq createFriendConnectionReq) {
        String error = RequestValidation.checkCreateFriendConnectionReq(createFriendConnectionReq);
        if (!error.equals("")) {
            throw new InputInvalidException(error);
        }
        return new ResponseEntity<>(new SuccessResponse
                (relationService.addFriend(createFriendConnectionReq)), HttpStatus.CREATED);

    }

    //retrieve friends list for an email address
    @PostMapping("/friends")
    public ResponseEntity<RetrieveFriendsListResponse> retrieveFriendsList(
            @Valid @RequestBody EmailRequest emailRequest) {
        if (emailRequest == null) {
            throw new InputInvalidException(ErrorConstraints.INVALID_REQUEST);
        }
        if (!EmailUtils.isEmail(emailRequest.getEmail())) {
            throw new InputInvalidException(ErrorConstraints.INVALID_EMAIL);
        }
        List<String> emails = relationService.retrieveFriendsList(emailRequest);
        return emails.isEmpty() ? ResponseEntity.noContent().build() : new ResponseEntity<>(new RetrieveFriendsListResponse
                (true, emails, emails.size()), HttpStatus.OK);
    }

    //retrieve the common friends list between two email addresses
    @PostMapping("/common")
    public ResponseEntity<RetrieveFriendsListResponse> getCommonFriends(
            @Valid @RequestBody CreateFriendConnectionReq friendRequest) {
        String error = RequestValidation.checkCreateFriendConnectionReq(friendRequest);
        if (!error.equals("")) {
            throw new InputInvalidException(error);
        }
        List<String> listEmails = relationService.getCommonFriends(friendRequest);
        return listEmails.isEmpty() ? ResponseEntity.noContent().build() : new ResponseEntity<>(new RetrieveFriendsListResponse
                (true, listEmails, listEmails.size()), HttpStatus.OK);
    }

    //to subscribe to update from an email address
    @PostMapping("/subscribe")
    public ResponseEntity<SuccessResponse> subscribeTo(
            @Valid @RequestBody SubscribeAndBlockRequest subscribeRequest) {
        String error = RequestValidation.checkSubscribeAndBlockRequest(subscribeRequest);
        if (!error.equals("")) {
            throw new InputInvalidException(error);
        }

        return new ResponseEntity<>(new SuccessResponse
                (relationService.subscribeTo(subscribeRequest)), HttpStatus.CREATED);

    }

    //to block updates from an email address
    @PutMapping("/block")
    public ResponseEntity<SuccessResponse> blockEmail(
            @Valid @RequestBody SubscribeAndBlockRequest subscribeRequest) {
        String error = RequestValidation.checkSubscribeAndBlockRequest(subscribeRequest);
        if (!error.equals("")) {
            throw new InputInvalidException(error);
        }

        return new ResponseEntity<>(new SuccessResponse
                (relationService.blockEmail(subscribeRequest)), HttpStatus.OK);

    }

    //to retrieve all email addresses that can receive updates from an email address
    @PostMapping("/retrieve")
    public ResponseEntity<RetrieveEmailResponse> retrieveEmail(
            @Valid @RequestBody RetrieveRequest retrieveRequest) {
        String error = RequestValidation.checkRetrieveRequest(retrieveRequest);
        if (!error.equals("")) {
            throw new InputInvalidException(error);
        }
        Set<String> setEmails = relationService.retrieveEmails(retrieveRequest);

        return setEmails.isEmpty() ? ResponseEntity.noContent().build() : new ResponseEntity<>(new RetrieveEmailResponse
                (true, setEmails), HttpStatus.OK);
    }

}
