package com.shibuya.backend.medicalappointmentportal.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "name cannot be null")
    @Size(min = 3, max = 50)
    private String name;

    @NotNull(message = "speciality cannot be null")
    private String speciality;

    @NotNull(message = "email cannot be null")
    @Email
    private String email;

    @NotNull(message = "password cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "phone cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    @Size(max = 10)
    private String phone;

    @ElementCollection
    private List<String> availableTimes;


}
