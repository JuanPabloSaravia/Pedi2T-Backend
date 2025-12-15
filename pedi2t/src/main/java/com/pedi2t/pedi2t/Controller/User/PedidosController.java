package com.pedi2t.pedi2t.Controller.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedi2t.pedi2t.DTO.CancelarPedidoDTO;
import com.pedi2t.pedi2t.DTO.CancelarPedidoResponseDTO;
import com.pedi2t.pedi2t.DTO.PedidosRealizadosResponseDTO;
import com.pedi2t.pedi2t.DTO.SeleccPedidoResponseDTO;
import com.pedi2t.pedi2t.DTO.SeleccionarPedidoDTO;
import com.pedi2t.pedi2t.Service.User.CancelarPedidoService;
import com.pedi2t.pedi2t.Service.User.PedidosRealizadosService;
import com.pedi2t.pedi2t.Service.User.SeleccionarPedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/Pedidos")
public class PedidosController {

    @Autowired
    private SeleccionarPedidoService seleccionarPedidoService;
    
    @Autowired
    private PedidosRealizadosService pedidosRealizadosService;
    
    @Autowired
    private CancelarPedidoService cancelarPedidoService;
    
    @PostMapping("/SeleccionarPedido")
    public ResponseEntity<?> seleccionarPedido(@RequestBody SeleccionarPedidoDTO seleccionarPedidoDTO) {
      
        SeleccPedidoResponseDTO response = seleccionarPedidoService.seleccionarPedido(seleccionarPedidoDTO);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/PedidosRealizados")
    public ResponseEntity<PedidosRealizadosResponseDTO> pedidosRealizados(@RequestParam Long usuarioId) {
        
        PedidosRealizadosResponseDTO response = pedidosRealizadosService.obtenerPedidosProximaSemana(usuarioId);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/CancelarPedido")
    public ResponseEntity<CancelarPedidoResponseDTO> cancelarPedido(@RequestBody CancelarPedidoDTO cancelarPedidoDTO) {
        
        CancelarPedidoResponseDTO response = cancelarPedidoService.cancelarPedido(cancelarPedidoDTO);
        
        return ResponseEntity.ok(response);
    }
}
