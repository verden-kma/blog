package edu.ukma.blog.exceptions.user;

public class UsernameMissingException extends RuntimeException {
    final String usernamePassed;

    public UsernameMissingException(String usernamePassed) {
        this.usernamePassed = usernamePassed;
    }

    @Override
    public String getMessage() {
        return "no user with username \"" + usernamePassed + "\" found";
    }
}
