package com.s3.friendsmanagement.controller;

import com.s3.friendsmanagement.model.User;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/emails")
public class RelationController {

    @Autowired
    private RelationService emailService;

    @PostMapping()
    public ResponseEntity<User> getUserByEmail(@RequestBody EmailRequest emailRequest) {
        User userData = emailService.findByEmail(String.valueOf(emailRequest));//Optional.ofNullable(emailService.findByEmail(String.valueOf(emailRequest)));
        if (userData != null) {
            return new ResponseEntity<User>(userData, HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }


}
