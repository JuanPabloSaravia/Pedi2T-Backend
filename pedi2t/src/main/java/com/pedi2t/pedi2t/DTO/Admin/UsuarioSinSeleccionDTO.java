package com.pedi2t.pedi2t.DTO.Admin;

import lombok.Data;

@Data
public class UsuarioSinSeleccionDTO {
    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
}
