package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.HistorialPedidosResponseDTO;
import com.pedi2t.pedi2t.DTO.HistorialPedidosResponseDTO.HistorialPedidoDTO;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.User.HistorialPedidosService;

@Service
public class HistorialPedidosServiceImpl implements HistorialPedidosService {

    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public HistorialPedidosResponseDTO obtenerHistorialPedidos(Long usuarioId) {
        
        // Verificar que el usuario existe
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado para id: " + usuarioId);
        }
        
        UsuarioEntity usuario = usuarioOpt.get();
        
        // Buscar todos los pedidos del usuario (historial completo)
        List<PedidoDia> pedidosDia = pedidoDiaRepository.findHistorialByUsuarioId(usuarioId);
        
        // Convertir a DTO
        List<HistorialPedidoDTO> pedidosDTO = new ArrayList<>();
        for (PedidoDia pedidoDia : pedidosDia) {
            HistorialPedidoDTO dto = convertirAHistorialPedidoDTO(pedidoDia);
            pedidosDTO.add(dto);
        }
        
        // Crear la respuesta
        HistorialPedidosResponseDTO response = new HistorialPedidosResponseDTO();
        response.setUsuarioId(usuarioId);
        response.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
        response.setPedidos(pedidosDTO);
        
        return response;
    }
    
    private HistorialPedidoDTO convertirAHistorialPedidoDTO(PedidoDia pedidoDia) {
        HistorialPedidoDTO dto = new HistorialPedidoDTO();
        dto.setIdPedido(pedidoDia.getPedidoEntity().getId());
        dto.setNombrePlato(pedidoDia.getPlato().getNombre());
        dto.setCategoria(pedidoDia.getPlato().getCategoria());
        dto.setFotoUrl(pedidoDia.getPlato().getImagenUrl());
        dto.setFechaEntrega(pedidoDia.getFechaEntrega());
        dto.setEstado(pedidoDia.getPedidoEntity().getEstado().name());
        
        return dto;
    }
}