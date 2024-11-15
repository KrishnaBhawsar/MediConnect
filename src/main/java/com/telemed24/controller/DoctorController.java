package com.telemed24.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.telemed24.emailservice.EmailServiceImpl;
import com.telemed24.exception.UserWithEmailAlreadyExistException;
import com.telemed24.model.Appointment;
import com.telemed24.model.Doctor;
import com.telemed24.model.Patient;
import com.telemed24.model.secondarymodel.TimeSlot;
import com.telemed24.service.AppointmentService;
import com.telemed24.service.DoctorService;
import com.telemed24.service.PatientService;
import com.telemed24.service.TimeSlotService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final TimeSlotService timeSlotService;
    private final AppointmentService appointmentService;
    private final EmailServiceImpl mailService;

    @GetMapping("/view-profile")
    public ResponseEntity<Doctor> viewProfile(HttpServletRequest request) {
        HttpSession session=request.getSession(false);

        if(session==null) {
            System.out.println("\nPatient is not logged-in Session= "+session);
            return new ResponseEntity<>(null,HttpStatus.OK);
        } else	System.out.println("Patient session already existed");

        String email=(String) session.getAttribute("USER_EMAIL");
        Doctor doctor=doctorService.findByEmail(email).get();
        System.out.println("Viewing doctor profile: "+email);
        System.out.println("View Profile: " + doctor);
        return new ResponseEntity<>(doctor,HttpStatus.OK);
    }

    @GetMapping("/getalldoctors")
    public ResponseEntity<List<Doctor>> getAllDoctors(HttpServletRequest request) {
        List<Doctor> doctors=doctorService.findAll();
        return new ResponseEntity<List<Doctor>>(doctors,HttpStatus.OK);
    }

    @GetMapping("/getdoctorby")
    public ResponseEntity<List<ResponseDoctor>> getDoctorBy(@RequestParam("searchBy") String searchBy,@RequestParam("value") String value) {
        List<Doctor> doctors=null;
        System.out.println("\nSearching doctor by "+searchBy+"\n value = "+value);

        if(searchBy.equals("name")) {
            doctors=doctorService.findByName(value);
        } else if(searchBy.equals("city")) {
            doctors=doctorService.findByCity(value);
        } else {
            doctors=doctorService.findBySpecialization(value);
        }

        System.out.println(doctors);
        List<ResponseDoctor> responseDoctors=new ArrayList<>();
        for(Doctor doctor:doctors) {
            ResponseDoctor responseDoctor = new ResponseDoctor(doctor.getId(), doctor.getSpecialization(), doctor.getCity(), doctor.getName());
            responseDoctors.add(responseDoctor);
        }
        return new ResponseEntity<List<ResponseDoctor>>(responseDoctors,HttpStatus.OK);
    }

    // Get all available Time slots of a particular doctor
    @GetMapping("/getavailableslots")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(@RequestParam("doctorId") int doctorId) {
        List<TimeSlot> slots=null;
        System.out.println("\nSearching available slots of doctor :"+doctorId);

        Doctor doctor=doctorService.findById(doctorId);
        slots=timeSlotService.extractAvailable(doctor);
        return new ResponseEntity<List<TimeSlot>>(slots,HttpStatus.OK);
    }

    // Booking slot
    @PutMapping("/bookslot")
    public ResponseEntity<String> bookSlot(@RequestParam("doctorId") String doctorIdString,
                                           @RequestParam("slotId") String slotIdString,
                                           HttpServletRequest request) {
        int doctorId=Integer.parseInt(doctorIdString);
        int slotId=Integer.parseInt(slotIdString);

        System.out.println("Booking slot of doctor:"+doctorId);

        HttpSession session=request.getSession(false);

        String patientEmail=(String) session.getAttribute("USER_EMAIL");

        System.out.println("Inside slot book");

        TimeSlot slot=timeSlotService.findById(slotId);
        Doctor doctor=doctorService.findById(doctorId);
        Patient patient=patientService.findByEmail(patientEmail).get();

        String doctorName=doctor.getName();
        String patientName=patient.getName();
        String clinicAddress=doctor.getAddress();
        String appointmentTime=slot.getStartTime().toString();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String appointmentDate = currentDate.format(formatter);

        Appointment appointment=new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDate(appointmentDate);
        appointment.setMode("OFFLINE");

        patient.setAppointments(appointment);
        doctor.setAppointments(appointment);

//        timeSlotDao.updateSlot(slotId);
        mailService.sendAppointmentConfirmationMail(patientEmail,patientName,doctorName,
                clinicAddress,appointmentDate,appointmentTime);
        return new ResponseEntity<String>("slot booked",HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Map<String,String> request,HttpServletRequest requestSession) throws ParseException  {
        System.out.println("\n in patient update");
        String name=request.get("name");
        String phoneNo=request.get("phoneNo");
        String city=request.get("city");
        String address=request.get("address");
        String email=(String) requestSession.getSession().getAttribute("USER_EMAIL");

        Doctor updatedDoctor=new Doctor();
        updatedDoctor.setName(name);
        updatedDoctor.setPhoneNo(phoneNo);
        updatedDoctor.setCity(city);
        updatedDoctor.setAddress(address);
        doctorService.update(email,updatedDoctor);
        return new ResponseEntity<String>("patient update",HttpStatus.OK);
    }

    @GetMapping("/getappointments")
    public ResponseEntity<List<Appointment>> getAppointments(HttpServletRequest request) {
        List<Appointment> appointments=null;
        HttpSession session=request.getSession(false);
        String doctorEmail=(String) session.getAttribute("USER_EMAIL");

        Doctor doctor=doctorService.findByEmail(doctorEmail).get();
        appointments=appointmentService.findAllByDoctor(doctor);

        return new ResponseEntity<List<Appointment>>(appointments,HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        System.out.println("\nDoctor logout");
        HttpSession session=request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the user's session
        }
        return new ResponseEntity<>("user logout",HttpStatus.OK);
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
    @PostMapping("/register")
    public ResponseEntity<Doctor> register(@RequestBody Doctor doctor) {
        System.out.println("Storing patient into db");
        System.out.println(doctor);

        //One exception may be doctor already present with given email
        doctorService.register(doctor);
        return new ResponseEntity<>(doctor,HttpStatus.OK);
    }
    @PostMapping("/emailexist")
    public ResponseEntity<String> checkEmailExists(@RequestBody Map<String,String> doctor) {
        System.out.println("Checking doctor exist in DB : "+doctor.get("email"));
        Optional<Doctor> optionalDoctor=doctorService.findByEmail(doctor.get("email"));
        System.out.println("dfgegwerg");
        if(optionalDoctor.isPresent())
            throw new UserWithEmailAlreadyExistException();
        return ResponseEntity.ok("user not exist");
    }
    @PostMapping("/addtimeslot")
    public ResponseEntity<String> addTimeSlot(@RequestBody List<TimeSlot> slots,@RequestParam("email") String email) {
        System.out.println("inside add time slot");
        timeSlotService.addSlot(slots,email);
        return ResponseEntity.ok("slots added");
    }

//    @GetMapping("/getonlinedoctor")
//    public ResponseEntity<List<Doctor>> getOnlineDoctors(@RequestParam("specialization") String specialization) {
//        List<Doctor> doctors=null;
//        doctors=doctorDao.extractOnineDoctor(specialization);
//        return new ResponseEntity<List<Doctor>>(doctors,HttpStatus.OK);
//    }
}