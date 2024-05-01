package org.example.sumatyw_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.UNAUTHORIZED, reason = "User is not authenticated")
public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }

    public UserNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
