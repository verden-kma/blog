package edu.ukma.blog.exceptions.server_internal;

public class ServerCriticalError extends Error {
    public ServerCriticalError(String message) { super(message);}
    public ServerCriticalError(Throwable cause) {
        super(cause);
    }
}
