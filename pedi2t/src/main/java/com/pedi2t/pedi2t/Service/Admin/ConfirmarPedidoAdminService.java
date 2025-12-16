package com.pedi2t.pedi2t.Service.Admin;

import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoDTO;
import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoResponseDTO;

public interface ConfirmarPedidoAdminService {
    
    ConfirmarPedidoResponseDTO confirmarPedido(ConfirmarPedidoDTO confirmarPedidoDTO);
}