package com.pedi2t.pedi2t.Service;

import com.pedi2t.pedi2t.DTO.NotificacionResponseDTO;
import java.util.List;

public interface NotificacionService {
    List<NotificacionResponseDTO> obtenerNotificacionesPorUsuario(Long usuarioId);

    void marcarComoLeida(Long notificacionId);

    void eliminarNotificacion(Long notificacionId);
}
