package com.pedi2t.pedi2t.Service.Admin;

import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosDTO;
import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosResponseDTO;

public interface EntregarPedidosAdminService {
    
    EntregarPedidosResponseDTO marcarPedidosComoEntregados(EntregarPedidosDTO entregarPedidosDTO);
}