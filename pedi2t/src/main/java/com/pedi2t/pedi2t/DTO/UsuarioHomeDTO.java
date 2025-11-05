package com.pedi2t.pedi2t.DTO;

import java.util.List;

public class UsuarioHomeDTO {

    private Long id;
    
    private List<PlatoDTO> platos;

    public UsuarioHomeDTO() {}

    public UsuarioHomeDTO(Long id, String nombre, String apellido, List<PlatoDTO> platos) {
        this.id = id;
       
        this.platos = platos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PlatoDTO> getPlatos() {
        return platos;
    }

    public void setPlatos(List<PlatoDTO> platos) {
        this.platos = platos;
    }
}
