package com.vaadin.example.spammingapp.backend.repository;

import com.vaadin.example.spammingapp.backend.entity.SEmial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SEmailRepository extends JpaRepository<SEmial, Long> {
}
