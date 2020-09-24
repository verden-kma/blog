package edu.ukma.blog.exceptions;

public class UsernameDuplicateException extends RuntimeException {
    public UsernameDuplicateException(String reason) {
        super(reason);
    }
}
