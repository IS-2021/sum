package org.example.sumatyw_backend.exceptions.advice;

import org.example.sumatyw_backend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, e.getFieldError().getDefaultMessage()
        );
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(InvalidDataException.class)
    public ProblemDetail handleInvalidData(InvalidDataException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()
        );
        problemDetail.setTitle(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ProblemDetail handleObjectNotFound(ObjectNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, e.getMessage()
        );
        problemDetail.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ProblemDetail handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, e.getMessage()
        );
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, e.getMessage()
        );
        problemDetail.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ProblemDetail handleUserNotAuthenticatedException(UserNotAuthenticatedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED, e.getMessage()
        );
        problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return problemDetail;
    }
}
