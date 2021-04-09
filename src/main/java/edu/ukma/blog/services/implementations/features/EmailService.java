package edu.ukma.blog.services.implementations.features;

import edu.ukma.blog.services.interfaces.features.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    @Value("${spring.mail.username}")
    private final String SENDER_EMAIL;
    @Value("${frontendConfirmationUrl}")
    private final String CONFIRM_SIGNUP_FRONT;
    private final JavaMailSender mailSender;

    @Override
    public void sendAccountActivation(String receiver, String username, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_EMAIL);
        message.setTo(receiver);
        message.setSubject(String.format("Dear %s, please confirm your registration.", username));
        message.setText(CONFIRM_SIGNUP_FRONT + token);
        mailSender.send(message);
    }
}
