package com.villysiu.yumtea.validation;

public class EmailExistsException extends RuntimeException {
  public EmailExistsException() {
    super();
  }

  public EmailExistsException(final String message) {

      super(message);
    }

//  public EmailExistsException(String message, Throwable cause) {
//    super(message, cause);
//  }

//  public EmailExistsException(Throwable cause) {
//    super(cause);
//  }
}
