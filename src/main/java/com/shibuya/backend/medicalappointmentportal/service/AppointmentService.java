package com.shibuya.backend.medicalappointmentportal.Service;

import com.shibuya.backend.medicalappointmentportal.Repository.AppointmentRepository;
import com.shibuya.backend.medicalappointmentportal.Repository.DoctorRepository;
import com.shibuya.backend.medicalappointmentportal.Repository.PatientRepository;
import com.shibuya.backend.medicalappointmentportal.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TokenService tokenService;

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());
        if (existing.isPresent()) {
            // Add validation logic if needed
            appointmentRepository.save(appointment);
            response.put("message", "Appointment updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            Long patientId = tokenService.extractPatientId(token);
            if (appointment.getPatientId().getId().equals(patientId)) {
                appointmentRepository.delete(appointment);
                response.put("message", "Appointment cancelled successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Unauthorized to cancel this appointment");
                return ResponseEntity.status(403).body(response);
            }
        } else {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> response = new HashMap<>();
        Long doctorId = tokenService.extractDoctorId(token);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments;
        if (pname == null || pname.equalsIgnoreCase("null") || pname.trim().isEmpty()) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        } else {
            appointments = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, pname, start, end);
        }
        response.put("appointments", appointments);
        return response;
    }
}

