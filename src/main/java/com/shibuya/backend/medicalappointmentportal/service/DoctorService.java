package com.shibuya.backend.medicalappointmentportal.Service;


import com.shibuya.backend.medicalappointmentportal.Repository.AppointmentRepository;
import com.shibuya.backend.medicalappointmentportal.Repository.DoctorRepository;
import com.shibuya.backend.medicalappointmentportal.model.Appointment;
import com.shibuya.backend.medicalappointmentportal.model.Doctor;
import com.shibuya.backend.medicalappointmentportal.Service.DTO.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return Collections.emptyList();

        Doctor doctor = doctorOpt.get();
        List<String> allSlots = doctor.getAvailableTimes();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        Set<String> bookedSlots = appointments.stream()
                .map(app -> app.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toSet());

        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) return -1;
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getId())) return -1;
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(long id) {
        if (!doctorRepository.existsById(id)) return -1;
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(LoginDTO login) {
        Doctor doctor = doctorRepository.findByEmail(login.getEmail());
        Map<String, String> response = new HashMap<>();
        if (doctor != null && doctor.getPassword().equals(login.getPassword())) {
            String token = tokenService.generateToken(doctor.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }

    public Map<String, Object> findDoctorByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return wrapFilteredDoctors(filterDoctorByTime(doctors, amOrPm));
    }

    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        return wrapFilteredDoctors(filterDoctorByTime(doctors, amOrPm));
    }

    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return wrapFilteredDoctors(doctors);
    }

    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return wrapFilteredDoctors(filterDoctorByTime(doctors, amOrPm));
    }

    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return wrapFilteredDoctors(doctors);
    }

    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        return wrapFilteredDoctors(filterDoctorByTime(doctors, amOrPm));
    }

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(doc -> doc.getAvailableTimes().stream()
                        .anyMatch(time -> amOrPm.equalsIgnoreCase("AM") ?
                                LocalTime.parse(time).isBefore(LocalTime.NOON) :
                                !LocalTime.parse(time).isBefore(LocalTime.NOON)))
                .collect(Collectors.toList());
    }

    private Map<String, Object> wrapFilteredDoctors(List<Doctor> doctors) {
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }
}

