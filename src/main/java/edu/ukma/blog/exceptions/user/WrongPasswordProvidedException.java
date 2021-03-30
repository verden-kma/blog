package edu.ukma.blog.exceptions.user;

public class WrongPasswordProvidedException extends RuntimeException {
    public WrongPasswordProvidedException(String message) {
        super(message);
    }
}
