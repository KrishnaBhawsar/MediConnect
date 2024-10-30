package com.telemed24.model.secondarymodel;

import com.telemed24.model.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Doctor doctor;
    private LocalTime startTime;
    private LocalTime endTime;
    private int currentNoOfPatient;
    private int maxPatientPerSlot;
}
