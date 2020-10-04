package edu.ukma.blog.exceptions;

public class ServerCriticalException extends Error {
    public ServerCriticalException(String message) { super(message);}
    public ServerCriticalException(Throwable cause) {
        super(cause);
    }
}
