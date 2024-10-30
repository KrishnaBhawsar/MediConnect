package com.telemed24.model;

import com.telemed24.model.secondarymodel.TimeSlot;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    @Column(unique = true)
    public String email;
    public String phoneNo;
    public String city;
    public String password;

    private String certificateNo;
    private int rating;
    private String specialization;
    private String address;
    private String modeOfConsultation;
    @OneToMany
    private List<Appointment> appointments=new ArrayList<>();
    @OneToMany
    private List<TimeSlot> appointmentTimeSlots=new ArrayList<>();

    public void setAppointments(Appointment appointment) {
        appointments.add(appointment);
    }
}
