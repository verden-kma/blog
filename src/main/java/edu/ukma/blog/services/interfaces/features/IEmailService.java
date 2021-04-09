package edu.ukma.blog.services.interfaces.features;

public interface IEmailService {
    void sendAccountActivation(String receiver, String username, String token);
}
