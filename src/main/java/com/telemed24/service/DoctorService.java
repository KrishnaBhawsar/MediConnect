package com.telemed24.service;

import com.telemed24.exception.UserWithEmailAlreadyExistException;
import com.telemed24.model.Doctor;
import com.telemed24.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService{
    private final DoctorRepository doctorRepository;

    public Doctor findById(int id) {
        return doctorRepository.findById(id);
    }
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
    public List<Doctor> findByName(String prefixName) {
        return doctorRepository.findByNameStartingWithIgnoreCase(prefixName);
    }
    public List<Doctor> findByCity(String city) {
        return doctorRepository.findByCity(city);
    }
    public List<Doctor> findBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }
    public void update(String email,Doctor updatedDoctor) {
        Optional<Doctor> doctor=doctorRepository.findByEmail(email);
        if(doctor.isPresent()) {
            Doctor existingDoctor=doctor.get();
            existingDoctor.setName(updatedDoctor.getName());
            existingDoctor.setPhoneNo(updatedDoctor.getPhoneNo());
            existingDoctor.setCity(updatedDoctor.getCity());
            existingDoctor.setAddress(updatedDoctor.getAddress());
            doctorRepository.save(existingDoctor);
        }
    }

    public void register(Doctor doctor) {
        try{
            doctorRepository.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new UserWithEmailAlreadyExistException();
        }
    }
}
