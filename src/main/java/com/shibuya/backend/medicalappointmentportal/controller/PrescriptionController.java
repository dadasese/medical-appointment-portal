package com.shibuya.backend.medicalappointmentportal.controller;

import com.shibuya.backend.medicalappointmentportal.service.PrescriptionService;
import com.shibuya.backend.medicalappointmentportal.service.Service;
import com.shibuya.backend.medicalappointmentportal.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private Service service;

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(@PathVariable String token,
                                                                @RequestBody Prescription prescription) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "doctor");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return validationResponse;
        }
        return prescriptionService.savePrescription(prescription);
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(@PathVariable Long appointmentId,
                                                               @PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "doctor");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(validationResponse.getStatusCode()).body(Map.of("message", "Unauthorized access"));
        }
        return prescriptionService.getPrescription(appointmentId);
    }
}
