package com.pedi2t.pedi2t.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioLoginDTO {
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasena;
}
