package com.pedi2t.pedi2t.Service.User;

import com.pedi2t.pedi2t.DTO.UsuarioHomeDTO;

public interface UsuarioHomeService {
    
    /**
     * Obtiene la información del usuario y sus pedidos asociados para mostrar en el home.
     * @param idUsuario ID del usuario logueado
     * @return UsuarioHomeDTO con los datos del usuario y la lista de pedidos
     */
    UsuarioHomeDTO obtenerDatosHome(Long idUsuario);
}
