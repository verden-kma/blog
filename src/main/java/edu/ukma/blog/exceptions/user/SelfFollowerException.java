package edu.ukma.blog.exceptions.user;

public class SelfFollowerException extends RuntimeException {
    public SelfFollowerException() {
        super("You cannot follow yourself.");
    }
}
