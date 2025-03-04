package com.villysiu.yumtea.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class EntityNotBelongToUserException extends RuntimeException {
    public EntityNotBelongToUserException(String message) {
        super(message);
    }
}
