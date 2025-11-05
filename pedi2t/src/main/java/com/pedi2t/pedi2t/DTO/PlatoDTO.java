package com.pedi2t.pedi2t.DTO;

public class PlatoDTO {

    private Long idPlato;
    private String nombre;
    private String descripcion;
    private String imagenUrl;

    public PlatoDTO() {}

    public PlatoDTO(Long idPlato, String nombre, String descripcion, String imagenUrl) {
        this.idPlato = idPlato;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    public Long getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(Long idPlato) {
        this.idPlato = idPlato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
