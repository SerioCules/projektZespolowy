package com.vaadin.example.spammingapp.backend.service;

import com.vaadin.example.spammingapp.backend.entity.Contact;
import com.vaadin.example.spammingapp.backend.entity.EmailCfg;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private EmailCfg emailCfg;

    public FeedbackController(EmailCfg emailCfg) {
        this.emailCfg = emailCfg;
    }

    @PostMapping
    public void sendFeedback(@RequestBody Contact contact/*,
                             BindingResult bindingResult*/){
/*        if(bindingResult.hasErrors()){
            throw new ValidationException("Feedback isn't valid");
        }*/
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());
        //Creating email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("mailtrap@mailtrap.com");
        mailMessage.setTo(contact.getToEmail());
        mailMessage.setSentDate(convertToDateViaSqlTimestamp(contact.getDate()));
        mailMessage.setSubject(contact.getEmailTopic());
        mailMessage.setText(contact.getMsgBody());

        mailSender.send(mailMessage);
    }
    public Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }
}
