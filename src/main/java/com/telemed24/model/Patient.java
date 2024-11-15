package com.telemed24.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    public int id;
    public String name;
    @Column(unique = true)
    public String email;
    public String phoneNo;
    public String city;
    public String password;

    private Date dob;
    @OneToMany
    private List<Appointment> appointments=new ArrayList<>();

    public void setAppointments(Appointment appointment) {
        appointments.add(appointment);
    }
}
