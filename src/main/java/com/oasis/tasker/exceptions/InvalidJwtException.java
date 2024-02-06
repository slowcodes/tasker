package com.oasis.tasker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtException extends AuthenticationException {
  public InvalidJwtException(String ex) {
    //super("Invalid JWT");
    super(ex);
  }
}
