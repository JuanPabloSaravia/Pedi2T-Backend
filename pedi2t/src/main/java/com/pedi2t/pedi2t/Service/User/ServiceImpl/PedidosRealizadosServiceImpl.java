package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.PedidosRealizadosResponseDTO;
import com.pedi2t.pedi2t.DTO.PedidosRealizadosResponseDTO.PedidoRealizadoDTO;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Enum.DiaSemanaEnum;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.User.PedidosRealizadosService;

@Service
public class PedidosRealizadosServiceImpl implements PedidosRealizadosService {

    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public PedidosRealizadosResponseDTO obtenerPedidosProximaSemana(Long usuarioId) {
        
        // Verificar que el usuario existe
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado para id: " + usuarioId);
        }
        
        UsuarioEntity usuario = usuarioOpt.get();
        
        // Calcular las fechas de la próxima semana (lunes a viernes)
        LocalDate hoy = LocalDate.now();
        LocalDate proximoLunes = hoy.with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate proximoViernes = proximoLunes.plusDays(4); // Lunes + 4 días = Viernes
        
        // Buscar los pedidos pendientes del usuario para la próxima semana
        List<PedidoDia> pedidosDia = pedidoDiaRepository.findByUsuarioIdAndFechaEntregaBetween(
            usuarioId, proximoLunes, proximoViernes
        );
        
        // Convertir a DTO (forma tradicional)
        List<PedidoRealizadoDTO> pedidosDTO = new ArrayList<>();
        for (PedidoDia pedidoDia : pedidosDia) {
            PedidoRealizadoDTO dto = convertirAPedidoRealizadoDTO(pedidoDia);
            pedidosDTO.add(dto);
        }
        
        // Crear la respuesta
        PedidosRealizadosResponseDTO response = new PedidosRealizadosResponseDTO();
        response.setUsuarioId(usuarioId);
        response.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
        response.setPedidos(pedidosDTO);
        
        return response;
    }
    
    private PedidoRealizadoDTO convertirAPedidoRealizadoDTO(PedidoDia pedidoDia) {
        PedidoRealizadoDTO dto = new PedidoRealizadoDTO();
        dto.setIdPedido(pedidoDia.getPedidoEntity().getId());
        dto.setFechaEntrega(pedidoDia.getFechaEntrega());
        dto.setEstado(pedidoDia.getPedidoEntity().getEstado().name());
        dto.setNombrePlato(pedidoDia.getPlato().getNombre());
        dto.setDescripcionPlato(pedidoDia.getPlato().getDescripcion());
        dto.setFotoUrl(pedidoDia.getPlato().getImagenUrl());
        dto.setIdMenuDia(pedidoDia.getMenuDia().getId());
        
        // Obtener el día de la semana en español sin acentos usando enum
        DayOfWeek diaSemana = pedidoDia.getFechaEntrega().getDayOfWeek();
        DiaSemanaEnum diaSemanaEnum = DiaSemanaEnum.fromDayOfWeek(diaSemana);
        dto.setDiaSemana(diaSemanaEnum.getNombre());
        
        return dto;
    }
}