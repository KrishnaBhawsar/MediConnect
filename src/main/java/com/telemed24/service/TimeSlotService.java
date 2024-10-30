package com.telemed24.service;

import com.telemed24.model.Doctor;
import com.telemed24.model.secondarymodel.TimeSlot;
import com.telemed24.repository.DoctorRepository;
import com.telemed24.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;

    public TimeSlot findById(int id) {
        return timeSlotRepository.findById(id).get();
    }
    public List<TimeSlot> extractAvailable(Doctor doctor) {
        return timeSlotRepository.findAllByStartTimeAfterAndDoctor(LocalTime.now(),doctor);
    }
    public void addSlot(List<TimeSlot> slots, String doctorEmail) {
        Doctor doctor=doctorRepository.findByEmail(doctorEmail).get();
        for(int i=0;i<slots.size();i++) {
            TimeSlot slot=slots.get(i);
            slot.setDoctor(doctor);
            timeSlotRepository.save(slot);
        }
    }
}
