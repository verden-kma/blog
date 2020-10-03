package edu.ukma.blog.exceptions;

public class UsernameDuplicateException extends RuntimeException {
    final String username;

    public UsernameDuplicateException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return "username \"" + username + "\" is already taken";
    }
}
