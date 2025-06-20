package com.shibuya.backend.medicalappointmentportal.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "preescriptions")
public class Prescription {

    @Id
    private String id;
    @NotNull(message = "appointment cannot be null")
    @Size(min = 3, max = 100)
    private Long appointmentId;

    @NotNull(message = "medication cannot be null")
    @Size(min = 3, max = 20)
    private String medication;

    @NotNull(message = "dosage cannot be null")
    @Size(max = 200)
    private String dosage;
}
