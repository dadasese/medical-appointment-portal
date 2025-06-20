package com.shibuya.backend.medicalappointmentportal.repository;

import com.shibuya.backend.medicalappointmentportal.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);

}