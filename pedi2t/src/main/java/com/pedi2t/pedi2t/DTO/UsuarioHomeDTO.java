package com.pedi2t.pedi2t.DTO;

import java.util.List;

public class UsuarioHomeDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private List<PlatoDTO> platos;

    public UsuarioHomeDTO() {}

    public UsuarioHomeDTO(Long id, String nombre, String apellido, List<PlatoDTO> platos) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.platos = platos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<PlatoDTO> getPlatos() {
        return platos;
    }

    public void setPlatos(List<PlatoDTO> platos) {
        this.platos = platos;
    }
}
