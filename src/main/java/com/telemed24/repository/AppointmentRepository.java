package com.telemed24.repository;

import com.telemed24.model.Appointment;
import com.telemed24.model.Doctor;
import com.telemed24.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    List<Appointment> findAllByDoctor(Doctor doctor);
    List<Appointment> findAllByPatient(Patient patient);
}
