package com.elearning.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPlainText(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("incolearnn@gmail.com");
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            // optional: setFrom should match spring.mail.username for some providers
            // msg.setFrom("youremail@gmail.com");
            mailSender.send(msg);
            System.out.println("Email sent to " + to);
        } catch (Exception e) {
            // don't fail enrollment if email fails
            System.err.println("Email send failed: " + e.getMessage());
        }
    }
}
