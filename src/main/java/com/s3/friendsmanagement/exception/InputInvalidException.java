package com.s3.friendsmanagement.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class InputInvalidException extends RuntimeException {

    public InputInvalidException(String message) {
        super(message);
    }

}
