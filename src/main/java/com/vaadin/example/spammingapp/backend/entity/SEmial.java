package com.vaadin.example.spammingapp.backend.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class SEmial extends AbstractEntity {
    private String email;
/*    private String spassword;*/

    @OneToMany(mappedBy = "semail", fetch = FetchType.EAGER)
    private List<Contact> employees = new LinkedList<>();

    public SEmial() {
    }

    public SEmial(String email) {
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.email = name;
    }

/*    public String getSpassword() {
        return spassword;
    }

    public void setSpassword(String spassword) {
        this.spassword = spassword;
    }*/

    public List<Contact> getEmployees() {
        return employees;
    }
/*    @Override
    public String toString() {
        return email + " " + spassword;
    }*/
}
