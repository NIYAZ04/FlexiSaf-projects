package com.example.AmbulanceServiceProvider.controllers;

import com.example.AmbulanceServiceProvider.models.user;

import com.example.AmbulanceServiceProvider.models.userTypes;
import com.example.AmbulanceServiceProvider.controllers.UserRepository;
import com.example.AmbulanceServiceProvider.security.CookieUtills;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.AmbulanceServiceProvider.security.PasswordUtills.genPass;

@RestController
@RequestMapping("/")

public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/add/{role}")
    @Operation(summary = "[ADMIN] Add a user to the database with a given role", description = "Adds a user with the specified role to the database. Requires admin privileges.")
    public user addUser(HttpServletResponse response,
                        @CookieValue(value = "Token", defaultValue = "") String token,
                        @RequestParam("FirstName") String fname,
                        @RequestParam("LastName") String lname,
                        @RequestParam("id") String id,
                        @PathVariable("role") String role) {
        CookieUtills cookieUtils = new CookieUtills();
        if (cookieUtils.isAdmin(token)) {
            try {
                userTypes role_assign=userTypes.GUEST;
                int flag=0;
                userTypes[] available_types = userTypes.values();
                for (userTypes role_x : available_types) {
                    if (role.equalsIgnoreCase(role_x.name())) {
                        role_assign = role_x;
                        flag = 1;
                    }
                }
                if (flag==0)
                {
                    response.setStatus(403);
                    return null;
                }
                String password = genPass(6);
                List<user> ll = userRepository.findByUserId(id);
                if (ll.isEmpty()) {
                    user User = new user(fname, lname, id);
                    User.setRole(role_assign);
                    User.setPassHash(password);
                    userRepository.save(User);

                    response.setStatus(201);
                    return User;
                }
                else {
                    response.setStatus(403);
                    return null;
                }
            } catch (Exception e) {
                response.setStatus(403);
                System.out.print(e.getStackTrace());
                return null;
            }
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @PostMapping("/add-doctor")
    @Operation(summary = "[ADMIN] Add a doctor to the database", description = "Adds a doctor to the database. Requires admin privileges.")    public user addDoctor(HttpServletResponse response,
                          @CookieValue(value = "Token", defaultValue = "") String token,
                          @RequestParam("FirstName") String fname,
                          @RequestParam("LastName") String lname,
                          @RequestParam("id") String id) {
        CookieUtills cookieUtils = new CookieUtills();
        if (cookieUtils.isAdmin(token)) {
            try {
                String password = genPass(6);
                List<user> ll = userRepository.findByUserId(id);
                if (ll.isEmpty()) {
                    user User = new user(fname, lname, id);
                    User.setRole(userTypes.DOCTOR);
                    User.setPassHash(password);
                    userRepository.save(User);

                    response.setStatus(201);
                    return User;
                }
                else {
                    response.setStatus(403);
                    return null;
                }
            } catch (Exception e) {
                response.setStatus(403);
                System.out.print(e.getStackTrace());
                return null;
            }
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @PostMapping("/add-employee")
    @Operation(summary = "[ADMIN] Add an employee to the database", description = "Adds an employee to the database. Requires admin privileges.")    public user addEmployee(
            HttpServletResponse response,
            @CookieValue(value = "Token", defaultValue = "") String token,
            @RequestParam("FirstName") String fname,
            @RequestParam("LastName") String lname,
            @RequestParam("id") String id) {
        CookieUtills cookieUtils = new CookieUtills();
        if (cookieUtils.isAdmin(token)) {
            try {
                String password = genPass(6);
                List<user> ll = userRepository.findByUserId(id);
                if (ll.isEmpty()) {
                    user User = new user(fname, lname, id);
                    User.setRole(userTypes.EMPLOYEE);
                    User.setPassHash(password);
                    userRepository.save(User);

                    response.setStatus(201);
                    return User;
                }
                else {
                    response.setStatus(403);
                    return null;
                }
            } catch (Exception e) {
                response.setStatus(403);
                System.out.print(e.getStackTrace());
                return null;
            }
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @PostMapping("/add-attendee")
    @Operation(summary = "[ADMIN] Add an attendee to the database", description = "Adds an attendee to the database. Requires admin privileges.")    public user addAttendee(HttpServletResponse response,
                            @CookieValue(value = "Token", defaultValue = "") String token,
                            @RequestParam( "FirstName") String fname,
                            @RequestParam( "LastName") String lname,
                            @RequestParam("id") String id) {
        CookieUtills cookieUtills = new CookieUtills();
        if (cookieUtills.isAdmin(token)) {
            try {
                String password = genPass(6);
                List<user> ll = userRepository.findByUserId(id);
                if (ll.isEmpty()) {
                    user User = new user(fname, lname, id);
                    User.setRole(userTypes.ATTENDEE);
                    User.setPassHash(password);
                    userRepository.save(User);

                    response.setStatus(201);
                    return User;
                }
                else {
                    response.setStatus(403);
                    return null;
                }

            }
            catch (Exception e) {
                response.setStatus(403);
                System.out.print(e.getStackTrace());
                return null;
            }
        } else {
            response.setStatus(401);
            return null;
        }
    }
}