package edu.ukma.blog.exceptions.user;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleUsernameDuplicate(UsernameDuplicateException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUsernameMissing(UsernameMissingException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNoSuchUserException(NoSuchUserException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNoSuchUserException(SelfFollowerException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleWrongPasswordException(WrongPasswordProvidedException e) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
