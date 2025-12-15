package com.pedi2t.pedi2t.Controller.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedi2t.pedi2t.DTO.SeleccPedidoResponseDTO;
import com.pedi2t.pedi2t.DTO.SeleccionarPedidoDTO;
import com.pedi2t.pedi2t.Service.User.SeleccionarPedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/Pedidos")
public class PedidosController {

    @Autowired
    private SeleccionarPedidoService seleccionarPedidoService;
    
    @PostMapping("/SeleccionarPedido")
    public ResponseEntity<?> seleccionarPedido(@RequestBody SeleccionarPedidoDTO seleccionarPedidoDTO) {
      
        SeleccPedidoResponseDTO response = seleccionarPedidoService.seleccionarPedido(seleccionarPedidoDTO);

        return ResponseEntity.ok(response);
    }
    
    
}
