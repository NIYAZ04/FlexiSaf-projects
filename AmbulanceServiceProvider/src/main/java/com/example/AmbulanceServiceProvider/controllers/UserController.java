package com.example.AmbulanceServiceProvider.controllers;

import com.example.AmbulanceServiceProvider.models.user;
import com.example.AmbulanceServiceProvider.models.UserDTO;
import com.example.AmbulanceServiceProvider.models.userTypes;
import com.example.AmbulanceServiceProvider.security.CookieUtills;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.AmbulanceServiceProvider.security.PasswordUtills.genPass;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/add/{role}")
    @Operation(summary = "[ADMIN] Add a user to the database with the given role")

    public user addUser(HttpServletResponse response,
                        @CookieValue(value = "Token", defaultValue = "") String token,
                        @RequestBody UserDTO userDTO,
                        @PathVariable("role") String role) {
        CookieUtills cookieUtills = new CookieUtills();
        if (cookieUtills.isAdmin(token)) {
            try {
                userTypes roleAssign = userTypes.GUEST;
                boolean roleFound = false;
                for (userTypes roleType : userTypes.values()) {
                    if (role.equalsIgnoreCase(roleType.name())) {
                        roleAssign = roleType;
                        roleFound = true;
                        break;
                    }
                }

                if (!roleFound) {
                    response.setStatus(403); // Forbidden: Role not found
                    return null;
                }

                List<user> existingUsers = userRepository.findByUserId(userDTO.getId());
                if (existingUsers.isEmpty()) {
                    user newUser = new user(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getId());
                    newUser.setRole(roleAssign);
                    newUser.setPassHash(genPass(6));
                    userRepository.save(newUser);
                    response.setStatus(201); // Created
                    return newUser;
                } else {
                    response.setStatus(403); // Forbidden: User ID already exists
                    return null;
                }
            } catch (Exception e) {
                response.setStatus(403); // Forbidden: Other exceptions
                e.printStackTrace();
                return null;
            }
        } else {
            response.setStatus(401); // Unauthorized: Not an admin
            return null;
        }
    }

    @PostMapping("/add-doctor")
    @Operation(summary = "[ADMIN] Add a doctor to the database")
    public user addDoctor(HttpServletResponse response,
                          @CookieValue(value = "Token", defaultValue = "") String token,
                          @RequestBody UserDTO userDTO) {
        return addUserWithSpecificRole(response, token, userDTO, userTypes.DOCTOR);
    }

    @PostMapping("/add-employee")
    @Operation(summary = "[ADMIN] Add an employee to the database")
    public user addEmployee(HttpServletResponse response,
                            @CookieValue(value = "Token", defaultValue = "") String token,
                            @RequestBody UserDTO userDTO) {
        return addUserWithSpecificRole(response, token, userDTO, userTypes.EMPLOYEE);
    }

    @PostMapping("/add-attendee")
    @Operation(summary = "[ADMIN] Add an attendee to the database")
    public user addAttendee(HttpServletResponse response,
                            @CookieValue(value = "Token", defaultValue = "") String token,
                            @RequestBody UserDTO userDTO) {
        return addUserWithSpecificRole(response, token, userDTO, userTypes.ATTENDEE);
    }

    private user addUserWithSpecificRole(HttpServletResponse response,
                                         String token,
                                         UserDTO userDTO,
                                         userTypes role) {
        CookieUtills cookieUtills = new CookieUtills();
        if (cookieUtills.isAdmin(token)) {
            try {
                List<user> existingUsers = userRepository.findByUserId(userDTO.getId());
                if (existingUsers.isEmpty()) {
                    user newUser = new user(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getId());
                    newUser.setRole(role);
                    newUser.setPassHash(genPass(6));
                    userRepository.save(newUser);
                    response.setStatus(201); // Created
                    return newUser;
                } else {
                    response.setStatus(403); // Forbidden: User ID already exists
                    return null;
                }
            } catch (Exception e) {
                response.setStatus(403); // Forbidden: Other exceptions
                e.printStackTrace();
                return null;
            }
        } else {
            response.setStatus(401); // Unauthorized: Not an admin
            return null;
        }
    }
}
