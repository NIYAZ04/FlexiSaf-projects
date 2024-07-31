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

public class AmbulanceController
{
    @Autowired
    AmbulanceRepository ambulanceRepository;
    @PostMapping(value="/add")
    @Operation(summary = " [ADMIN] Add an ambulance to database")
    public ambulance addNewAmbulance(
            HttpServletResponse response,
            @CookieValue(value = "Token", defaultValue = "") String token,
            @RequestParam("NumberPlate") String np,
            @RequestParam("isAvailable") String isAvailable)
    {

        CookieUtills cookieUtills =new CookieUtills();
        if(cookieUtills.isAdmin(token)) {
            try {
                ambulance Ambulance = new ambulance(np,
                        isAvailable.equalsIgnoreCase("true") ? availability.AVAILABLE : availability.UNAVAILABLE);
                ambulanceRepository.save(Ambulance);
                return Ambulance;
            }
            catch (Exception e)
            {
                response.setStatus(403);
                System.out.print(e.getStackTrace());
                return null;
            }

        }
        else {
            response.setStatus(401);
            return null;
        }
    }

    @PostMapping("/all")
    @Operation(summary = "Get all ambulance details")
    public List<ambulance> getAllAmbulances(HttpServletResponse response,
                                            @CookieValue(value = "Token", defaultValue = "") String token)
    {
        CookieUtills cookieUtills =new CookieUtills();
        if(cookieUtills.isLoggedin(token)) {
            try {
                response.setStatus(200);
                return ambulanceRepository.findAll();
            }
            catch (Exception e)
            {
                response.setStatus(404);
                System.out.print(e.getStackTrace());
                return null;
            }
        }
        else {
            response.setStatus(401);
            return null;
        }
    }

    @PostMapping("/findavailable")
    @Operation(summary ="Find all available ambulances")
    public List<ambulance> findAvailable(HttpServletResponse response,
                                         @CookieValue(value = "Token", defaultValue = "") String token)
    {
        CookieUtills cookieUtills =new CookieUtills();
        if(cookieUtills.isLoggedin(token)) {
            response.setStatus(200);
            return ambulanceRepository.findByStatus(availability.AVAILABLE);
        }
        else {
            response.setStatus(401);
            return null;
        }
    }

    @PostMapping(value = "/find/{NumberPlate}")
    @Operation(summary ="List of available ambulance")
    public ambulance find(HttpServletResponse response,
                          @CookieValue(value = "Token", defaultValue = "")
                          String token,@PathVariable("NumberPlate") String np)
    {
        CookieUtills cookieUtills =new CookieUtills();
        if(cookieUtills.isLoggedin(token)) {
            try {
                response.setStatus(200);
                return ambulanceRepository.findByNumberplate(np).get(0);
            }
            catch (Exception e) {
                response.setStatus(404);
                System.out.print(e.getStackTrace());
                return null;
            }
        }
        else {
            response.setStatus(401);
            return null;
        }

    }

    @PutMapping("/available/{NumberPlate}")
    @Operation(summary = " [ADMIN] Make an ambulance available")
    public ambulance makeAvailable( HttpServletResponse response,
                                    @CookieValue(value = "Token", defaultValue = "") String token,
                                    @PathVariable("NumberPlate") String np) {
        CookieUtills cookieUtills =new CookieUtills();
        if(cookieUtills.isAdmin(token)) {
            try{
                ambulance Ambulance = ambulanceRepository.findByNumberplate(np).get(0);
                Ambulance.setStatus(availability.AVAILABLE);
                ambulanceRepository.save(Ambulance);
                response.setStatus(200);
                return Ambulance;
            }
            catch (Exception e)
            {
                response.setStatus(404);
                System.out.print(e.getStackTrace());
                return null;
            }
        }
        else {
            response.setStatus(401);
            return null;
        }
    }

    @PutMapping("/unavailable/{NumberPlate}")
    @Operation(summary = " [ADMIN] Make an ambulance Unavailable")
    public ambulance makeUnavailable( HttpServletResponse response,
                                      @CookieValue(value = "Token", defaultValue = "") String token,
                                      @PathVariable("NumberPlate") String np) {
        CookieUtills cookieUtills =new CookieUtills();
        if(cookieUtills.isAdmin(token)) {
            try {
                ambulance Ambulance = ambulanceRepository.findByNumberplate(np).get(0);
                Ambulance.setStatus(availability.UNAVAILABLE);
                ambulanceRepository.save(Ambulance);
                response.setStatus(200);
                return Ambulance;
            }
            catch (Exception e)
            {
                response.setStatus(404);
                System.out.print(e.getStackTrace());
                return null;
            }
        }
        else {
            response.setStatus(401);
            return null;
        }
    }
}
