package com.pedi2t.pedi2t.Service.Admin;

public interface PublicarPlatoAdminService {
    void republicarPlato(Long usuarioId, Long menuPlatoId);
    void republicarTodosLosPlatos(Long usuarioId);
    void despublicarPlato(Long usuarioId, Long menuPlatoId);
    void despublicarTodosLosPlatos(Long usuarioId);
}