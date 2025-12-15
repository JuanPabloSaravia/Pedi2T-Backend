package com.pedi2t.pedi2t.DTO;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioPerfilResponseDTO {
	private Long id;
	private String nombre;
	private String apellido;
	private String email;
	private String direccion;
	private String telefono;
	private String rol;
	private List<String> diasPresenciales;
}
