package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.CancelarPedidoDTO;
import com.pedi2t.pedi2t.DTO.CancelarPedidoResponseDTO;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Entity.PedidoEntity;
import com.pedi2t.pedi2t.Enum.EstadoPedido;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.PedidoRepository;
import com.pedi2t.pedi2t.Service.User.CancelarPedidoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CancelarPedidoServiceImpl implements CancelarPedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;
    
    @Autowired
    private MenuDiaRepository menuDiaRepository;

    @Override
    public CancelarPedidoResponseDTO cancelarPedido(CancelarPedidoDTO cancelarPedidoDTO) {
        
        Long idPedido = cancelarPedidoDTO.getIdPedido();
        Long idUsuario = cancelarPedidoDTO.getIdUsuario();
        
        // Verificar que el pedido existe
        Optional<PedidoEntity> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido no encontrado para ID: " + idPedido);
        }
        
        PedidoEntity pedido = pedidoOpt.get();
        
        // Verificar que el pedido pertenece al usuario
        if (!pedido.getUsuario().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("No tienes permisos para cancelar este pedido");
        }
        
        // Verificar que el pedido esté en estado PENDIENTE
        if (!pedido.getEstado().equals(EstadoPedido.PENDIENTE)) {
            throw new IllegalArgumentException("Solo se pueden cancelar pedidos en estado PENDIENTE. Estado actual: " + pedido.getEstado());
        }
        
        // Buscar el PedidoDia asociado para recuperar el stock
        Optional<PedidoDia> pedidoDiaOpt = pedidoDiaRepository.findByPedidoEntityId(idPedido);
        if (pedidoDiaOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró información del pedido para restaurar stock");
        }
        
        PedidoDia pedidoDia = pedidoDiaOpt.get();
        
        // Restaurar el stock del menú
        var menuDia = pedidoDia.getMenuDia();
        menuDia.setStockTotal(menuDia.getStockTotal() + 1);
        menuDiaRepository.save(menuDia);
        
        // Cambiar el estado del pedido a CANCELADO
        pedido.setEstado(EstadoPedido.CANCELADO);
        PedidoEntity pedidoCancelado = pedidoRepository.save(pedido);
        
        // Crear la respuesta
        CancelarPedidoResponseDTO response = new CancelarPedidoResponseDTO();
        response.setIdPedido(pedidoCancelado.getId());
        response.setEstado(pedidoCancelado.getEstado().name());
        response.setMensaje("Pedido cancelado exitosamente");
        response.setFechaEntrega(pedidoDia.getFechaEntrega());
        response.setNombrePlato(pedidoDia.getPlato().getNombre());
        
        return response;
    }
}