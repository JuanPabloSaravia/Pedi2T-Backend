package com.pedi2t.pedi2t.Service.User;

import com.pedi2t.pedi2t.DTO.CancelarPedidoDTO;
import com.pedi2t.pedi2t.DTO.CancelarPedidoResponseDTO;

public interface CancelarPedidoService {
    
    CancelarPedidoResponseDTO cancelarPedido(CancelarPedidoDTO cancelarPedidoDTO);
}