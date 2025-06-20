package com.shibuya.backend.medicalappointmentportal.service;

import com.shibuya.backend.medicalappointmentportal.repository.AdminRepository;
import com.shibuya.backend.medicalappointmentportal.repository.DoctorRepository;
import com.shibuya.backend.medicalappointmentportal.repository.PatientRepository;
import com.shibuya.backend.medicalappointmentportal.model.Admin;
import com.shibuya.backend.medicalappointmentportal.model.Doctor;
import com.shibuya.backend.medicalappointmentportal.model.Patient;
import com.shibuya.backend.medicalappointmentportal.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey signingKey;

    public TokenService(AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @PostConstruct
    public void init() {
        this.signingKey = getSigningKey();
    }
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000L)) // 7 days
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }


    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validates a token against the expected role and returns a map with error if invalid.
     */
    public Map<String, String> validateToken(String token, String expectedRole) {
        Map<String, String> response = new HashMap<>();

        try {
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            if (!expectedRole.equals(role)) {
                response.put("error", "Role mismatch.");
                return response;
            }

            boolean isValid;
            if (role.equals("admin")) {
                Optional<Admin> admin = Optional.ofNullable(adminRepository.findByUsername(email));
                isValid = admin.isPresent();
            } else if (role.equals("doctor")) {
                Optional<Doctor> doctor = Optional.ofNullable(doctorRepository.findByEmail(email));
                isValid = doctor.isPresent();
            } else {
                response.put("error", "Unsupported role.");
                return response;
            }

            if (!isValid) {
                response.put("error", "Invalid user.");
            }

        } catch (Exception e) {
            response.put("error", "Invalid token: " + e.getMessage());
        }

        return response;
    }

    /**
     * Extracts the token from the Authorization header of an HTTP request.
     */
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /**
     * Extracts the patient ID from the token.
     * @param token JWT token
     * @return patient ID if found, otherwise null
     */
    public Long extractPatientId(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Patient> patient = Optional.ofNullable(patientRepository.findByEmail(email));
            return patient.map(Patient::getId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extracts the doctor ID from the token.
     * @param token JWT token
     * @return doctor ID if found, otherwise null
     */
    public Long extractDoctorId(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Doctor> doctor = Optional.ofNullable(doctorRepository.findByEmail(email));
            return doctor.map(Doctor::getId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}