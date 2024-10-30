package com.telemed24.emailservice;

public interface EmailService {
    public String sendOtp(String to);
    public void sendAppointmentConfirmationMail(String to,String patientName,String doctorName,String address,String date,String time);
}

