package com.s3.friendsmanagement.controller;

import com.s3.friendsmanagement.exception.InputInvalidException;
import com.s3.friendsmanagement.exception.StatusException;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.response.RetrieveFriendsListResponse;
import com.s3.friendsmanagement.payload.response.SuccessResponse;
import com.s3.friendsmanagement.service.RelationService;
import com.s3.friendsmanagement.utils.EmailUtils;
import com.s3.friendsmanagement.utils.ErrorConstraints;
import com.s3.friendsmanagement.utils.RequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
        try {
            return new ResponseEntity<>(new SuccessResponse
                    (String.valueOf(relationService.addFriend(createFriendConnectionReq))), HttpStatus.CREATED);
        } catch (StatusException statusException){
            return new ResponseEntity<>(new SuccessResponse(statusException.getMessage()), HttpStatus.BAD_REQUEST);
        }

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
        //return ResponseEntity.noContent().build();
        return new ResponseEntity<>(new RetrieveFriendsListResponse
                (true, emails, emails.size()), HttpStatus.OK);
    }

}
