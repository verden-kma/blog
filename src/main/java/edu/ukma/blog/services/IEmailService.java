package edu.ukma.blog.services;

public interface IEmailService {
    void sendAccountActivation(String receiver, String username, String token);
}
