package com.pedi2t.pedi2t.DTO.Admin;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class CargarPlatosRequestDTO {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 1, max = 100, message = "La descripción debe tener entre 1 y 100 caracteres")
    private String descripcion;

    @NotBlank(message = "La categoría no puede estar vacía")
    @Size(min = 1, max = 50, message = "La categoría debe tener entre 1 y 50 caracteres")
    private String categoria;
    
    @NotEmpty(message = "Debe seleccionar al menos un día de la semana")
    private List<String> diasSemana; // ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"]
    
    // La URL de la imagen se asignará automáticamente después de subir a Cloudinary
    private String imagenUrl;
}
