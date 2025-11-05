package com.pedi2t.pedi2t.DTO; // (Usa tu paquete)

import lombok.Data;

@Data
public class LoginResponseDTO {
    
    private String token;
    private String nombre;
    private String apellido;

    
}