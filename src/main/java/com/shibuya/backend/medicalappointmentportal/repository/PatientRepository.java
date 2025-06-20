package com.shibuya.backend.medicalappointmentportal.repository;

import com.shibuya.backend.medicalappointmentportal.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find a patient by their email
    Patient findByEmail(String email);

    // Find a patient by either their email or phone number
    Patient findByEmailOrPhone(String email, String phone);
}

