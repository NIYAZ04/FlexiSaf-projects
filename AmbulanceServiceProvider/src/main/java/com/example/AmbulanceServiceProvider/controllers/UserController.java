package com.example.AmbulanceServiceProvider.controllers;



import com.example.AmbulanceServiceProvider.models.user;
import com.example.AmbulanceServiceProvider.models.UserDTO;
import com.example.AmbulanceServiceProvider.models.userTypes;

import com.example.AmbulanceServiceProvider.controllers.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.AmbulanceServiceProvider.security.PasswordUtills.genPass;
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/add/{role}")
    @Operation(summary = "Add a user to the database with the given role")
    public user addUser(HttpServletResponse response,
                        @RequestBody UserDTO userDTO,
                        @PathVariable("role") String role) {
        try {
            userTypes roleAssign = userTypes.GUEST;
            boolean roleFound = false;

            // Check if the provided role exists in the userTypes enum
            for (userTypes roleType : userTypes.values()) {
                if (role.equalsIgnoreCase(roleType.name())) {
                    roleAssign = roleType;
                    roleFound = true;
                    logger.info("Role found: {}", roleAssign);
                    break;
                }
            }

            // If the role is not found, respond with 403 Forbidden
            if (!roleFound) {
                logger.warn("Role not found: {}", role);
                response.setStatus(403); // Forbidden: Role not found
                return null;
            }

            // Check if a user with the provided ID already exists
            List<user> existingUsers = userRepository.findByUserId(userDTO.getId());
            if (existingUsers.isEmpty()) {
                // Create a new user and set the role
                user newUser = new user(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getId());
                newUser.setRole(roleAssign);
                newUser.setPassHash(genPass(6)); // Generate a password hash
                userRepository.save(newUser);
                logger.info("User created: {}", newUser);
                response.setStatus(201); // Created
                return newUser;
            } else {
                logger.warn("User ID already exists: {}", userDTO.getId());
                response.setStatus(403); // Forbidden: User ID already exists
                return null;
            }
        } catch (Exception e) {
            logger.error("Error creating user", e);
            response.setStatus(403); // Forbidden: Other exceptions
            return null;
        }
    }

    @PostMapping("/add-doctor")
    @Operation(summary = "Add a doctor to the database")
    public user addDoctor(HttpServletResponse response,
                          @RequestBody UserDTO userDTO) {
        return addUserWithSpecificRole(response, userDTO, userTypes.DOCTOR);
    }

    @PostMapping("/add-employee")
    @Operation(summary = "Add an employee to the database")
    public user addEmployee(HttpServletResponse response,
                            @RequestBody UserDTO userDTO) {
        return addUserWithSpecificRole(response, userDTO, userTypes.EMPLOYEE);
    }

    @PostMapping("/add-attendee")
    @Operation(summary = "Add an attendee to the database")
    public user addAttendee(HttpServletResponse response,
                            @RequestBody UserDTO userDTO) {
        return addUserWithSpecificRole(response, userDTO, userTypes.ATTENDEE);
    }

    private user addUserWithSpecificRole(HttpServletResponse response,
                                         UserDTO userDTO,
                                         userTypes role) {
        try {
            List<user> existingUsers = userRepository.findByUserId(userDTO.getId());
            if (existingUsers.isEmpty()) {
                user newUser = new user(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getId());
                newUser.setRole(role);
                newUser.setPassHash(genPass(6));
                userRepository.save(newUser);
                logger.info("User created with role {}: {}", role, newUser);
                response.setStatus(201); // Created
                return newUser;
            } else {
                logger.warn("User ID already exists: {}", userDTO.getId());
                response.setStatus(403); // Forbidden: User ID already exists
                return null;
            }
        } catch (Exception e) {
            logger.error("Error creating user with role " + role, e);
            response.setStatus(403); // Forbidden: Other exceptions
            return null;
        }
    }
}
