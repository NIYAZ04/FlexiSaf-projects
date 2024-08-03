package com.example.AmbulanceServiceProvider.controllers;

import com.example.AmbulanceServiceProvider.models.ambulance;
import com.example.AmbulanceServiceProvider.models.availability;
import com.example.AmbulanceServiceProvider.controllers.AmbulanceRepository;
import com.example.AmbulanceServiceProvider.security.CookieUtills;

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

    @PostMapping(value="/add")
    @Operation(summary = " [ADMIN] Add an ambulance to database")
    public ambulance addNewAmbulance(
            HttpServletResponse response,
            @CookieValue(value = "Token", defaultValue = "") String token,
            @RequestParam("NumberPlate") String np,
            @RequestParam("isAvailable") String isAvailable) {
        try {
            if (!isAdmin(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
            ambulance Ambulance = new ambulance(np,
                    isAvailable.equalsIgnoreCase("true") ? availability.AVAILABLE : availability.UNAVAILABLE);
            ambulanceRepository.save(Ambulance);
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
    @Operation(summary = " [ADMIN] Make an ambulance Available")
    public ambulance makeAvailable(HttpServletResponse response,
                                   @CookieValue(value = "Token", defaultValue = "") String token,
                                   @PathVariable("NumberPlate") String np) {
        return updateAmbulanceAvailability(response, token, np, availability.AVAILABLE);
    }

    @PutMapping("/unavailable/{NumberPlate}")
    @Operation(summary = " [ADMIN] Make an ambulance Unavailable")
    public ambulance makeUnavailable(HttpServletResponse response,
                                     @CookieValue(value = "Token", defaultValue = "") String token,
                                     @PathVariable("NumberPlate") String np) {
        return updateAmbulanceAvailability(response, token, np, availability.UNAVAILABLE);
    }

    private boolean isAdmin(String token) {
        CookieUtills cookieUtills = new CookieUtills();
        return cookieUtills.isAdmin(token);
    }

    private ambulance updateAmbulanceAvailability(HttpServletResponse response, String token, String np, availability status) {
        try {
            if (!isAdmin(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
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