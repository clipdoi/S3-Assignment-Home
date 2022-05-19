package com.s3.friendsmanagement.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class StatusException extends RuntimeException {

    public StatusException(String message) {
        super(message);
    }

}
