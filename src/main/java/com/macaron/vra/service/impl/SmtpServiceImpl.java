package com.macaron.vra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmtpServiceImpl {


    private JavaMailSender mailSender;

    @Autowired
    public SmtpServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String from, List<String> to, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to.toArray(new String[to.size()]));
        message.setSubject("Server Service Error");
        message.setText(body);

        mailSender.send(message);
    }
}
