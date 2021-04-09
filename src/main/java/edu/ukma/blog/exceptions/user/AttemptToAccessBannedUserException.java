package edu.ukma.blog.exceptions.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttemptToAccessBannedUserException extends RuntimeException {
    private final String username;

    @Override
    public String getMessage() {
        return String.format("User %s is banned and cannot be accessed.", username);
    }
}
