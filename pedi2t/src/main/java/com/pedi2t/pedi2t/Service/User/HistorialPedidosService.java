package com.pedi2t.pedi2t.Service.User;

import com.pedi2t.pedi2t.DTO.HistorialPedidosResponseDTO;

public interface HistorialPedidosService {
    
    HistorialPedidosResponseDTO obtenerHistorialPedidos(Long usuarioId);
}