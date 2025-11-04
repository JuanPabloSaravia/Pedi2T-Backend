package com.pedi2t.pedi2t.DTO; // (Usa tu paquete)

public class LoginResponseDTO {
    
    private String token;

    // Constructor
    public LoginResponseDTO(String token) {
        this.token = token;
    }

    // --- Getter y Setter ---
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}