package com.telemed24.controller;

import com.telemed24.model.Doctor;
import com.telemed24.model.Patient;
import com.telemed24.service.DoctorService;
import com.telemed24.service.PatientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String,String> requestBody,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {

        HttpSession session=request.getSession();
        if(session.isNew()) {
            System.out.println("\nUser New Session");
        } else {
            System.out.println("\nUser session already existed");
        }

        System.out.println(requestBody.get("user")+" to login");
        System.out.println("login detail: email = "+requestBody.get("email")+
                "\n               password = "+requestBody.get("password")+"\n");

        String responseString="login successful";
        try {
            if (requestBody.get("user").equals("PATIENT")) {
                Patient patient = patientService.findByEmail(requestBody.get("email")).get();
                if (!requestBody.get("password").equals(patient.getPassword()))
                    responseString = "Incorrect password";
            } else {
                Doctor doctor = doctorService.findByEmail(requestBody.get("email")).get();
                if (!requestBody.get("password").equals(doctor.getPassword()))
                    responseString = "Incorrect password";
            }

            session.setAttribute("USER_EMAIL", requestBody.get("email"));
            session.setAttribute("USER_MODE", requestBody.get("user"));

            Cookie cookie = new Cookie("USER_MODE", requestBody.get("user"));
            response.addCookie(cookie);
        } catch (NoSuchElementException exception) {
            responseString="User Not Found";
        } finally {
            System.out.println(responseString);
            return new ResponseEntity<>(responseString, HttpStatus.OK);
        }
    }
}
