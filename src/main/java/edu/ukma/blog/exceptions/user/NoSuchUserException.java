package edu.ukma.blog.exceptions.user;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(long id) {
        super("No user with id " + id + " found.");
    }

    public NoSuchUserException(String username) {
        super("No user with nik " + username + " found.");
    }
}
