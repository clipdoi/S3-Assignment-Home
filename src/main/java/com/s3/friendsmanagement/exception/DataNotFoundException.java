package com.s3.friendsmanagement.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String message) {
        super(message);
    }

}
