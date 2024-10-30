package com.telemed24.service;

import com.telemed24.exception.UserWithEmailAlreadyExistException;
import com.telemed24.model.Patient;
import com.telemed24.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
    public void register(Patient patient) {
        try{
            patientRepository.save(patient);
        } catch (DataIntegrityViolationException e) {
            throw new UserWithEmailAlreadyExistException();
        }
     }
}
