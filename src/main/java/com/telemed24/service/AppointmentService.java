package com.telemed24.service;

import com.telemed24.model.Appointment;
import com.telemed24.model.Doctor;
import com.telemed24.model.Patient;
import com.telemed24.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public List<Appointment> findAllByDoctor(Doctor doctor) {
        return appointmentRepository.findAllByDoctor(doctor);
    }
    public List<Appointment> findAllByPatient(Patient patient) {
        return appointmentRepository.findAllByPatient(patient);
    }
    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }
}
