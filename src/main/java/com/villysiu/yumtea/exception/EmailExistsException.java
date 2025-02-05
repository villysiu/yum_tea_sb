package com.villysiu.yumtea.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailExistsException extends RuntimeException {
  public EmailExistsException() {
    super();
  }

//  public EmailExistsException(final String message) {
//
//      super(message);
//    }

}
