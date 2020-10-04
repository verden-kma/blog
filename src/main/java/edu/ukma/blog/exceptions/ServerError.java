package edu.ukma.blog.exceptions;

public class ServerError extends Error {
    public ServerError(String message) { super(message);}
    public ServerError(Throwable cause) {
        super(cause);
    }
}
