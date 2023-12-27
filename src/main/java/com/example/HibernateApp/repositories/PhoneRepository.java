package com.example.HibernateApp.repositories;

import com.example.HibernateApp.models.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

}