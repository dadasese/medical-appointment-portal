package com.shibuya.backend.medicalappointmentportal.Service.DTO;

import com.shibuya.backend.medicalappointmentportal.model.Appointment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentDTO {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    private LocalDateTime appointmentTime;
    private int status;
    private LocalDate appointmentDate;
    private LocalTime appointmentTimeOnly;
    private LocalDateTime endTime;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.doctorId = appointment.getDoctorId().getId();
        this.doctorName = appointment.getDoctorId().getName();
        this.patientId = appointment.getPatientId().getId();
        this.patientName = appointment.getPatientId().getName();
        this.patientEmail = appointment.getPatientId().getEmail();
        this.patientPhone = appointment.getPatientId().getPhone();
        this.patientAddress = appointment.getPatientId().getAddress();
        this.appointmentTime = appointment.getAppointmentTime();
        this.status = appointment.getStatus();
        this.appointmentDate = appointment.getAppointmentTime().toLocalDate();
        this.appointmentTimeOnly = appointment.getAppointmentTime().toLocalTime();
        this.endTime = appointment.getAppointmentTime().plusHours(1);
    }
}

