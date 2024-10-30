package com.telemed24.repository;

import com.telemed24.model.Doctor;
import com.telemed24.model.secondarymodel.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot,Integer> {
    Optional<TimeSlot> findById(int id);
    List<TimeSlot> findAllByStartTimeAfterAndDoctor(LocalTime currentTime, Doctor Doctor);

}
