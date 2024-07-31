package com.example.AmbulanceServiceProvider.controllers;

import com.example.AmbulanceServiceProvider.models.ambulance;
import com.example.AmbulanceServiceProvider.models.availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmbulanceRepository extends JpaRepository<ambulance, String> {
    @Override
    List<ambulance> findAll();

    List<ambulance> findByNumberplate(String numberplate);

    List<ambulance> findByStatus(availability availability);
}
