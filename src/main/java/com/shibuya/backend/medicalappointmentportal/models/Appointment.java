package com.shibuya.backend.medicalappointmentportal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "doctor cannot be null")
    @ManyToOne
    private Doctor doctorId;

    @NotNull(message = "patient cannot be null")
    @ManyToOne
    private Patient patientId;

    @Future
    private LocalDateTime appointmentTime;

    private int status;

    @Transient
    private LocalDateTime getEndTime(){
        return appointmentTime.plusHours(1);
    }
    @Transient
    private Date getAppointmentDate(){
        return Timestamp.valueOf(appointmentTime);
    }
    @Transient
    private LocalTime getAppointmentDateOnly(){
        return appointmentTime.toLocalTime();
    }
}
