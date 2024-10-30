package com.telemed24.model;

import com.telemed24.model.secondarymodel.TimeSlot;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    private String date;
    private String prescription;
    private String mode;
}
