package com.pedi2t.pedi2t.DTO.Admin;

import lombok.Data;

@Data
public class PlatoAdminDTO {
    private Long idPlato;
    private Long idMenuPlato; // ID del MenuPlatosEntity
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private String categoria;
    private Boolean publicado; // Campo específico para admin
}