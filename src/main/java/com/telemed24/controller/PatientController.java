package com.telemed24.controller;

import com.telemed24.emailservice.EmailServiceImpl;
import com.telemed24.exception.UserWithEmailAlreadyExistException;
import com.telemed24.model.Appointment;
import com.telemed24.model.Patient;
import com.telemed24.service.AppointmentService;
import com.telemed24.service.DoctorService;
import com.telemed24.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final EmailServiceImpl mailService;
    private final AppointmentService appointmentService;

    // View Profile
    @GetMapping("/view-profile")
    public ResponseEntity<Patient> viewProfile(HttpServletRequest request) {
        HttpSession session=request.getSession(false);

        if(session==null) {
            System.out.println("\nPatient is not logged-in Session= "+session);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else	System.out.println("Patient session already existed");

        String email=(String) session.getAttribute("USER_EMAIL");
        Patient patient=patientService.findByEmail(email).get();
        System.out.println("Viewing patient profile: "+email);
        System.out.println("View Profile: " + patient);
        return new ResponseEntity<>(patient,HttpStatus.OK);
    }

    // Logout API
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        System.out.println("\nPatient logout");
        HttpSession session = request.getSession(false); // Get the current session without creating a new one
        if (session != null) {
            session.invalidate(); // Invalidate the user's session
        }
        return new ResponseEntity<String>("user logout",HttpStatus.OK);
    }

    @PostMapping("/reqOTP")
    public ResponseEntity<String> requestOtp(@RequestBody Map<String,String> requestBody)
    {
        String to=requestBody.get("email");
        System.out.println("Sending otp to "+to);
        String otp=mailService.sendOtp(to);
        System.out.println("otp = "+otp);  // working

        return new ResponseEntity<String>(otp,HttpStatus.OK);
    }

    @PostMapping("/emailexist")
    public ResponseEntity<String> checkEmailExists(@RequestBody Map<String,String> request) {
        System.out.println("Checking patient exist in DB : "+request.get("email"));
        Optional<Patient> optionalPatient=patientService.findByEmail(request.get("email"));
        System.out.println("dfghdfgdfg");
        if(optionalPatient.isPresent()) {
            throw new UserWithEmailAlreadyExistException();
        }
        else return ResponseEntity.ok("user not exist");
    }

    @PostMapping ("/register")
    public ResponseEntity<String> register(@RequestBody Patient patient) {
        System.out.println("Storing patient into db");
        System.out.println(patient);

        //One exception may be patient already present with given email
        patientService.register(patient);

        return new ResponseEntity<>("sign-up successfull",HttpStatus.OK);
    }

//    @PutMapping("/update")
//    public ResponseEntity<String> update(@RequestBody Map<String,String> request,HttpServletRequest requestSession) throws ParseException {
//        System.err.println("\n in patient update");
//        String name=(String) request.get("name");
//        String phoneNo=(String) request.get("phoneNo");
//        String city=(String) request.get("city");
//        Date dob=new SimpleDateFormat("yyyy-mm-dd").parse(request.get("dob"));
//        String email=(String) requestSession.getSession().getAttribute("USER_EMAIL");
//
//        Optional<Patient> optionalPatient=patientService.findByEmail(email);
//        Patient patient=optionalPatient.get();
//        patient.setName(name);
//        patient.setPhoneNo(phoneNo);
//        patient.setDob(dob);
//        patient.setCity(city);
//        patientDao.update(patient);
//        return new ResponseEntity<String>("patient update",HttpStatus.OK);
//    }


    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Setter
    @Getter
    static class ViewAppointment {
        private String doctorName;
        private LocalDate date;
        private LocalTime time;
        private  String location;
        private String specialization;
    }
//     To extract all appointments of patient
    @GetMapping("/getappointments")
    public ResponseEntity<List<ViewAppointment>> getAppointments(HttpServletRequest request) {
        List<Appointment> appointments=null;
        List<ViewAppointment> viewAppointments=new ArrayList<>();

        HttpSession session=request.getSession(false);

        if(session==null) {
            System.out.println("\nPatient is not logged-in Session= "+ null);
            return new ResponseEntity<>(null,HttpStatus.OK);
        } else	System.out.println("Patient session already existed");
        String patientEmail=(String) session.getAttribute("USER_EMAIL");
        System.out.println(patientEmail);
        Patient patient=patientService.findByEmail(patientEmail).get();
        System.out.println("Patinet = "+patient);

        appointments=appointmentService.findAllByPatient(patient);
        System.out.println("appointments = "+appointments);
        for(Appointment appointment:appointments) {
            ViewAppointment viewAppointment=new ViewAppointment();
            viewAppointment.setDoctorName(appointment.getDoctor().getName());
            viewAppointment.setDate(LocalDate.now());
            viewAppointment.setTime(LocalTime.parse(appointment.getTime()));
            viewAppointment.setLocation(appointment.getDoctor().getAddress());
            viewAppointment.setSpecialization(appointment.getDoctor().getSpecialization());
            viewAppointments.add(viewAppointment);
        }

        return ResponseEntity.ok(viewAppointments);
    }
}

