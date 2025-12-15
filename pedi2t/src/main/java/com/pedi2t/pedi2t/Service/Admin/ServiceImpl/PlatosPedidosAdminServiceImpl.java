package com.pedi2t.pedi2t.Service.Admin.ServiceImpl;

import com.pedi2t.pedi2t.DTO.Admin.PlatosPedidosResponseDTO;
import com.pedi2t.pedi2t.DTO.Admin.PlatosPedidosResponseDTO.PlatoPedidoDTO;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Enum.EstadoPedido;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Service.Admin.PlatosPedidosAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlatosPedidosAdminServiceImpl implements PlatosPedidosAdminService {

    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;

    @Override
    public PlatosPedidosResponseDTO obtenerPlatosPedidos() {
        
        // Una sola query que agrupa y cuenta directamente en la BD
        List<Object[]> resultados = pedidoDiaRepository.obtenerResumenPlatosPedidos();
        
        if (resultados.isEmpty()) {
            PlatosPedidosResponseDTO response = new PlatosPedidosResponseDTO();
            response.setPlatos(List.of());
            response.setTotalPedidos(0);
            response.setMensaje("No hay pedidos activos en este momento");
            return response;
        }
        
        // Mapear directamente desde los resultados de la query
        List<PlatoPedidoDTO> platosDTO = resultados.stream()
            .map(row -> {
                PlatoPedidoDTO dto = new PlatoPedidoDTO();
                dto.setIdPlato((Long) row[0]);           // plato.id
                dto.setNombrePlato((String) row[1]);     // plato.nombre
                dto.setCategoria((String) row[2]);       // plato.categoria
                dto.setImagenUrl((String) row[3]);       // plato.imagenUrl
                
                int pendientes = ((Long) row[4]).intValue();    // COUNT pendientes
                int confirmados = ((Long) row[5]).intValue();   // COUNT confirmados
                
                dto.setCantidadPendiente(pendientes);
                dto.setCantidadConfirmado(confirmados);
                dto.setCantidadTotal(pendientes + confirmados);
                
                return dto;
            })
            .collect(Collectors.toList());
        
        // Obtener total directamente de la BD (más eficiente)
        Long totalPedidosLong = pedidoDiaRepository.obtenerTotalPedidosActivos();
        int totalPedidos;
        if (totalPedidosLong != null) {
            totalPedidos = totalPedidosLong.intValue();
        } else {
            totalPedidos = 0;
        }
        
        // Crear respuesta
        PlatosPedidosResponseDTO response = new PlatosPedidosResponseDTO();
        response.setPlatos(platosDTO);
        response.setTotalPedidos(totalPedidos);
        response.setMensaje("Listado de platos pedidos obtenido exitosamente");
        
        return response;
    }
}