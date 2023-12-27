package com.example.HibernateApp.repositories;

import com.example.HibernateApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}