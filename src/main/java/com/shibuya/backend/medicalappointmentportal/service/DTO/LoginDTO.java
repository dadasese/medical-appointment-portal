package com.shibuya.backend.medicalappointmentportal.service.DTO;

public class LoginDTO {

    private String email;
    private String password;

    public LoginDTO() {
        // Default constructor
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

