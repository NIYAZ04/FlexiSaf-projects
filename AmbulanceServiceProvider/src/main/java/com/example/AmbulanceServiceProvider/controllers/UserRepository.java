package com.example.AmbulanceServiceProvider.controllers;

import com.example.AmbulanceServiceProvider.models.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<user, Long> {
    List<user> findAll();
    List<user> findByUserId(String userId);
    user findByFirstName(String firstName);
}