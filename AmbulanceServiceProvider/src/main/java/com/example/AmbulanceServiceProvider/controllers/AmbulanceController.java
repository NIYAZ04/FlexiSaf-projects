package com.example.AmbulanceServiceProvider.controllers;

import com.example.AmbulanceServiceProvider.models.ambulance;
import com.example.AmbulanceServiceProvider.models.availability;
import com.example.AmbulanceServiceProvider.controllers.AmbulanceRepository;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/ambulance")
public class AmbulanceController {

    @Autowired
    AmbulanceRepository ambulanceRepository;

    @PostMapping(value = "/add")
    @Operation(summary = "[ADMIN] Add an ambulance to database")
    public ambulance addNewAmbulance(
            HttpServletResponse response,
            @RequestParam("NumberPlate") String np,
            @RequestParam("isAvailable") String isAvailable) {
        try {
            ambulance Ambulance = new ambulance(np,
                    isAvailable.equalsIgnoreCase("true") ? availability.AVAILABLE : availability.UNAVAILABLE);
            ambulanceRepository.save(Ambulance);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return Ambulance;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all ambulance details")
    public List<ambulance> getAllAmbulances() {
        return ambulanceRepository.findAll();
    }

    @PutMapping("/available/{NumberPlate}")
    @Operation(summary = "[ADMIN] Make an ambulance Available")
    public ambulance makeAvailable(HttpServletResponse response,
                                   @PathVariable("NumberPlate") String np) {
        return updateAmbulanceAvailability(response, np, availability.AVAILABLE);
    }

    @PutMapping("/unavailable/{NumberPlate}")
    @Operation(summary = "[ADMIN] Make an ambulance Unavailable")
    public ambulance makeUnavailable(HttpServletResponse response,
                                     @PathVariable("NumberPlate") String np) {
        return updateAmbulanceAvailability(response, np, availability.UNAVAILABLE);
    }

    private ambulance updateAmbulanceAvailability(HttpServletResponse response, String np, availability status) {
        try {
            ambulance Ambulance = ambulanceRepository.findByNumberplate(np).get(0);
            Ambulance.setStatus(status);
            ambulanceRepository.save(Ambulance);
            response.setStatus(HttpServletResponse.SC_OK);
            return Ambulance;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            e.printStackTrace();
            return null;
        }
    }
}
