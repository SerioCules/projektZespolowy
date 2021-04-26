package com.vaadin.example.spammingapp.backend.service;

import com.vaadin.example.spammingapp.backend.entity.Contact;
import com.vaadin.example.spammingapp.backend.entity.SEmial;
import com.vaadin.example.spammingapp.backend.repository.ContactRepository;
import com.vaadin.example.spammingapp.backend.repository.SEmailRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactService {
    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
    private ContactRepository contactRepository;
    private SEmailRepository sEmailRepository;

    public ContactService(ContactRepository contactRepository,
                          SEmailRepository sEmailRepository) {
        this.contactRepository = contactRepository;
        this.sEmailRepository = sEmailRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }
    public List<Contact> findAll(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return contactRepository.findAll();
        }else {
                return contactRepository.search(filterText);
        }
    }

    public long count() {
        return contactRepository.count();
    }

    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public void save(Contact contact) {
        if (contact == null) {
            LOGGER.log(Level.SEVERE,
                    "Empty");
            return;
        }
        contactRepository.save(contact);
    }
    @PostConstruct
    public void populateTestData() {
        if (sEmailRepository.count() == 0) {
            sEmailRepository.saveAll(
                    Stream.of("mailtrap@mailtrap.com 123341141")
                            .map(name -> {
                                String[] split = name.split(" ");
                                SEmial sEmail = new SEmial();
                                sEmail.setEmail(split[0]);
/*                                sEmail.setSpassword(split[1]);*/
                                return sEmail;
                            })
                            .collect(Collectors.toList()));
        }
/*        if (contactRepository.count() == 0) {
            Random r = new Random(0);
            List<SEmial> companies = sEmailRepository.findAll();
            contactRepository.saveAll(
                    Stream.of("Gabrielle Patel", "Brian Robinson", "Eduardo Haugen",
                            "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson",
                            "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
                            "Eileen Walker", "Katelyn Mar   tin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
                            "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin",
                            "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
                            "Jaydan Jackson", "Bernard Nilsen")
                            .map(name -> {
                                String[] split = name.split(" ");
                                Contact contact = new Contact();
                                contact.setEmailTopic(split[0]);
                                contact.setMsgBody(split[1]);
                                contact.setSemail_id(companies.get(r.nextInt(companies.size())));
                                String email = (contact.getEmailTopic() + "." + contact.getMsgBody() + "@" + contact.getMsgBody().replaceAll("[\\s-]", "") + ".com").toLowerCase();
                                contact.setToEmail(email);
                                return contact;
                            }).collect(Collectors.toList()));
        }*/
    }

}