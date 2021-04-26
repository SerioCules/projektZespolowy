package com.vaadin.example.spammingapp.backend.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Contact extends AbstractEntity implements Cloneable {


    @NotNull
    @NotEmpty
    private String emailTopic = "";

    @NotNull
    @NotEmpty
    private String msgBody = "";

    @ManyToOne
    @JoinColumn(name = "semail_id")
    private SEmial semail;

    @Email
    @NotNull
    @NotEmpty
    private String toEmail = "";


    private LocalDateTime date;


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String email) {
        this.toEmail = email;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getEmailTopic() {
        return emailTopic;
    }

    public void setEmailTopic(String emailTopic) {
        this.emailTopic = emailTopic;
    }

    public void setSemail_id(SEmial semail) {
        this.semail = semail;
    }

    public SEmial getSemail_id() {
        return semail;
    }

    @Override
    public String toString() {
        return emailTopic + " " + msgBody;
    }

}
