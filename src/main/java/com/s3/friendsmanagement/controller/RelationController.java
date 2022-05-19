package com.s3.friendsmanagement.controller;

import com.s3.friendsmanagement.exception.InputInvalidException;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.response.SuccessResponse;
import com.s3.friendsmanagement.service.RelationService;
import com.s3.friendsmanagement.utils.RequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/emails")
public class RelationController {

    @Autowired
    private RelationService emailService;

//    @PostMapping()
//    public ResponseEntity<User> getUserByEmail(@RequestBody EmailRequest emailRequest) {
//        User userData = emailService.findByEmail(emailRequest.getEmail());
//        if (userData != null) {
//            return new ResponseEntity<User>(userData, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
//        }
//    }

    // create a friend connection between two email addresses
    @PostMapping("/add")
    public ResponseEntity<SuccessResponse> addFriend(
            @Valid @RequestBody CreateFriendConnectionReq createFriendConnectionReq) {
        String error = RequestValidation.checkCreateFriendConnectionReq(createFriendConnectionReq);
        if (!error.equals("")) {
            throw new InputInvalidException(error);
        }
        return new ResponseEntity<>(new SuccessResponse
                (emailService.addFriend(createFriendConnectionReq)), HttpStatus.CREATED);
    }

}
