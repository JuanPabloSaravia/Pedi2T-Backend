package com.pedi2t.pedi2t.Service.Admin;

import com.pedi2t.pedi2t.DTO.Admin.UsuarioSinSeleccionDTO;
import java.util.List;

public interface NotificacionAdminService {
    List<UsuarioSinSeleccionDTO> obtenerUsuariosSinSeleccion();

    void notificarUsuario(Long usuarioId);

    void notificarTodos();
}
