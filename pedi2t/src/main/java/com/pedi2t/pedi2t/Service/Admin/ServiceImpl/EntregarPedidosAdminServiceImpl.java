package com.pedi2t.pedi2t.Service.Admin.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosDTO;
import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosResponseDTO;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Enum.EstadoPedido;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.PedidoRepository;
import com.pedi2t.pedi2t.Service.Admin.EntregarPedidosAdminService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EntregarPedidosAdminServiceImpl implements EntregarPedidosAdminService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;

    @Override
    public EntregarPedidosResponseDTO marcarPedidosComoEntregados(EntregarPedidosDTO entregarPedidosDTO) {
        
        // Buscar todos los pedidos confirmados para la fecha especificada
        List<PedidoDia> pedidosConfirmados = pedidoDiaRepository.findPedidosConfirmadosByFecha(
            entregarPedidosDTO.getFechaEntrega()
        );
        
        if (pedidosConfirmados.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron pedidos confirmados para la fecha: " + entregarPedidosDTO.getFechaEntrega());
        }
        
        // Marcar todos los pedidos como ENTREGADO
        int cantidadEntregados = 0;
        for (PedidoDia pedidoDia : pedidosConfirmados) {
            var pedido = pedidoDia.getPedidoEntity();
            pedido.setEstado(EstadoPedido.ENTREGADO);
            pedidoRepository.save(pedido);
            cantidadEntregados++;
        }
        
        // Crear la respuesta
        EntregarPedidosResponseDTO response = new EntregarPedidosResponseDTO();
        response.setCantidadPedidosEntregados(cantidadEntregados);
        response.setMensaje("Se marcaron " + cantidadEntregados + " pedidos como entregados");
        response.setFechaEntrega(entregarPedidosDTO.getFechaEntrega());
        
        return response;
    }
}