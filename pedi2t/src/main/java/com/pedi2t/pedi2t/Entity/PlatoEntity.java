package com.pedi2t.pedi2t.Entity;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "platos")
public class PlatoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "la descripcion no puede estar vacío")
    @Size(min = 1, max = 100, message = "La descripcion debe tener entre 1 y 100 caracteres")
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotBlank(message = "La URL de la imagen no puede estar vacía")
    @Size(max = 2048, message = "La URL de la imagen no puede exceder los 2048 caracteres")
    @URL(protocol = "https", message = "Debe ser una URL HTTPS válida") // Valida el formato
    @Column(name = "imagen_url", length = 2048) // 'length' optimiza la columna en la BD
    private String imagenUrl;

    @NotBlank(message = "la categoria no puede estar vacío")
    @Size(min = 1, max = 50, message = "La categoria debe tener entre 1 y 50 caracteres")
    @Column(name = "categoria", nullable = false)
    private String categoria;

}
