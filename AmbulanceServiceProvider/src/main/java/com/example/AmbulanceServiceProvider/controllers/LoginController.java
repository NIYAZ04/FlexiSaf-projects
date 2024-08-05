package com.example.AmbulanceServiceProvider.controllers;

import com.example.AmbulanceServiceProvider.models.user;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import static com.example.AmbulanceServiceProvider.security.PasswordUtills.checkPass;
import static com.example.AmbulanceServiceProvider.security.PasswordUtills.genPass;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    @Operation(summary = "Login", description = "Logs in a user with given ID and password")
    public String login(HttpServletResponse response, @RequestParam("id") String id, @RequestParam("pass") String pass) {
        try {
            user User = userRepository.findByUserId(id).get(0);
            if (checkPass(User.getPassHash(), pass)) {
                response.setStatus(200);
                return "Welcome " + User.getFirstName() + " " + User.getLastName();
            } else {
                response.setStatus(403);
                return "Incorrect Password";
            }
        } catch (Exception e) {
            response.setStatus(404);
            return "No User exists for given Id: " + id;
        }
    }

    @DeleteMapping("/logout")
    @Operation(summary = "Logout", description = "Logs out a user")
    public String logout(HttpServletResponse response) {
        response.setStatus(200);
        return "Logged Out";
    }

    @PutMapping("/resetPassword")
    @Operation(summary = "Resetting password of a user", description = "Resets password for a user")
    public String resetPassword(HttpServletResponse response, @RequestParam("id") String id) {
        try {
            user User = userRepository.findByUserId(id).get(0);
            String password = genPass(6);
            User.setPassHash(password);
            userRepository.save(User);
            response.setStatus(201);
            return "Password for user: " + User.toString() + " changed to: " + password;
        } catch (Exception e) {
            response.setStatus(404);
            return "No User exists for given Id: " + id;
        }
    }

    @PutMapping("/changePassword")
    @Operation(summary = "Changing password by user", description = "Allows a user to change their password")
    public String changePassword(HttpServletResponse response,
                                 @RequestParam("id") String id,
                                 @RequestParam("Old_pass") String Old_pass,
                                 @RequestParam("new_pass") String new_pass) {
        try {
            user User = userRepository.findByUserId(id).get(0);
            if (checkPass(User.getPassHash(), Old_pass)) {
                User.setPassHash(new_pass);
                userRepository.save(User);
                response.setStatus(201);
                return "Password Successfully Changed for " + User.getFirstName() + " " + User.getLastName();
            } else {
                response.setStatus(403);
                return "Old Password Incorrect";
            }
        } catch (Exception e) {
            response.setStatus(404);
            return "No User exists for given Id: " + id;
        }
    }
}
