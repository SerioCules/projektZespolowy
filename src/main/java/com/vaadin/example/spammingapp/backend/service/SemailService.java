package com.vaadin.example.spammingapp.backend.service;

import com.vaadin.example.spammingapp.backend.entity.SEmial;
import com.vaadin.example.spammingapp.backend.repository.SEmailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemailService {

    private SEmailRepository sEmailRepository;

    public SemailService(SEmailRepository sEmailRepository) {
        this.sEmailRepository = sEmailRepository;
    }

    public List<SEmial> findAll() {
        return sEmailRepository.findAll();
    }

}
