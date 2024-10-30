package com.telemed24.repository;

import com.telemed24.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Integer> {
    Doctor findById(int id);
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findByNameStartingWithIgnoreCase(String prd);
    List<Doctor> findByCity(String city);
    List<Doctor> findBySpecialization(String specialization);
}
