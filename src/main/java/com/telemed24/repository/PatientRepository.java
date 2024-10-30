package com.telemed24.repository;

import com.telemed24.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Optional<Patient> findByEmail(String email);
}
