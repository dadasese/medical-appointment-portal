package com.shibuya.backend.medicalappointmentportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "name cannot be null")
    private String name;

    @NotNull(message = "email cannot be null")
    @Email
    private String email;

    @NotNull(message = "password cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "phone cannot be null")
    @Pattern(regexp = "\\{10}", message = "Phone must be 10 digits")
    @Size(min=10)
    private String phone;

    @NotNull(message = "address cannot be null")
    @Pattern(regexp = "\\{10}")
    private String address;
}
