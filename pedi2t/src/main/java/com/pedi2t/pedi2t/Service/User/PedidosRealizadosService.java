package com.pedi2t.pedi2t.Service.User;

import com.pedi2t.pedi2t.DTO.PedidosRealizadosResponseDTO;

public interface PedidosRealizadosService {
    
    PedidosRealizadosResponseDTO obtenerPedidosProximaSemana(Long usuarioId);
}