package com.shibuya.backend.medicalappointmentportal.Service;

import com.shibuya.backend.medicalappointmentportal.Repository.AdminRepository;
import com.shibuya.backend.medicalappointmentportal.Repository.DoctorRepository;
import com.shibuya.backend.medicalappointmentportal.Repository.PatientRepository;
import com.shibuya.backend.medicalappointmentportal.model.Admin;
import com.shibuya.backend.medicalappointmentportal.model.Appointment;
import com.shibuya.backend.medicalappointmentportal.model.Doctor;
import com.shibuya.backend.medicalappointmentportal.model.Patient;
import com.shibuya.backend.medicalappointmentportal.Service.DTO.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = tokenService.validateToken(token, user);
        if (!response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (admin != null && admin.getPassword().equals(receivedAdmin.getPassword())) {
            String token = tokenService.generateToken(admin.getEmail());
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("error", "Invalid credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        if (name != null && specialty != null && time != null) {
            return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
        } else if (name != null && specialty != null) {
            return doctorService.filterDoctorByNameAndSpecility(name, specialty);
        } else if (name != null && time != null) {
            return doctorService.filterDoctorByNameAndTime(name, time);
        } else if (specialty != null && time != null) {
            return doctorService.filterDoctorByTimeAndSpecility(specialty, time);
        } else if (specialty != null) {
            return doctorService.filterDoctorBySpecility(specialty);
        } else if (time != null) {
            return doctorService.filterDoctorsByTime(time);
        } else if (name != null) {
            return doctorService.findDoctorByName(name);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctorService.getDoctors());
            return response;
        }
    }

    public int validateAppointment(Appointment appointment) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctorId().getId());
        if (doctorOpt.isEmpty()) {
            return -1;
        }
        Long doctorId = appointment.getDoctorId().getId();
        String appointmentTime = appointment.getAppointmentTime().toLocalTime().toString();
        return doctorService.getDoctorAvailability(doctorId, appointment.getAppointmentTime().toLocalDate())
                .contains(appointmentTime) ? 1 : 0;
    }

    public boolean validatePatient(Patient patient) {
        return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(LoginDTO login) {
        Map<String, String> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(login.getEmail());
        if (patient != null && patient.getPassword().equals(login.getPassword())) {
            String token = tokenService.generateToken(login.getEmail());
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("error", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        Long patientId = tokenService.extractPatientId(token);
        if (name != null && condition != null) {
            return patientService.filterByDoctorAndCondition(condition, name, patientId);
        } else if (name != null) {
            return patientService.filterByDoctor(name, patientId);
        } else if (condition != null) {
            return patientService.filterByCondition(condition, patientId);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid filter criteria");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
