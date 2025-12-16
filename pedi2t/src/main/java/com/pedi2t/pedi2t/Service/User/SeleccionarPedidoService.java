package com.pedi2t.pedi2t.Service.User;

import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.SeleccPedidoResponseDTO;
import com.pedi2t.pedi2t.DTO.SeleccionarPedidoDTO;

@Service
public interface SeleccionarPedidoService {

    public SeleccPedidoResponseDTO seleccionarPedido(SeleccionarPedidoDTO seleccionarPedidoDTO);
    
}
