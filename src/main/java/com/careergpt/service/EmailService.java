package com.careergpt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.reset-password-url}")
    private String resetPasswordUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = resetPasswordUrl + token;
        String subject = "Password Reset Request";
        String text = "We received a request to reset the password for your Clareer account.\n" +
        "If you made this request, please click the link below to set a new password:\n\n" +

        resetLink +

        "\nThis link will expire in 10 minutes for security reasons. If the link has expired, \n" +
        "you can request a new one from the Forgot Password page.\n" +
        "\nIf you didn’t request a password reset, please ignore this email — your account will remain secure.\n" +

        "\nStay secure,\n" +
        "The Clareer Team";
//        String text = "To reset your password, click the link below:\n" + resetLink +
//                "\n\nThis link will expire in 10 minutes.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}