package org.example.sumatyw_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ReverseGeocodeException extends RuntimeException {
    public ReverseGeocodeException(String message) {
        super(message);
    }

    public ReverseGeocodeException(String message, Throwable cause) {
        super(message, cause);
    }

}
