package com.pedi2t.pedi2t.Service.Admin.ServiceImpl;

import com.pedi2t.pedi2t.DTO.Admin.PlatosPedidosResponseDTO;
import com.pedi2t.pedi2t.DTO.Admin.PlatosPedidosResponseDTO.PlatoPedidoDTO;
import com.pedi2t.pedi2t.DTO.Admin.PlatosPedidosResponseDTO.PedidosPorDiaDTO;
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
            response.setPedidosPorDia(List.of());
            response.setTotalPedidos(0);
            response.setMensaje("No hay pedidos activos en este momento");
            return response;
        }
        
        // Mapear y agrupar por día de la semana
        Map<String, List<PlatoPedidoDTO>> platosPorDia = resultados.stream()
            .map(row -> {
                PlatoPedidoDTO dto = new PlatoPedidoDTO();
                dto.setIdPlato((Long) row[0]);
                dto.setNombrePlato((String) row[1]);
                dto.setCategoria((String) row[2]);
                dto.setImagenUrl((String) row[3]);
                
                String diaSemana = (String) row[4];
                int pendientes = ((Long) row[5]).intValue();
                int confirmados = ((Long) row[6]).intValue();
                
                dto.setCantidadPendiente(pendientes);
                dto.setCantidadConfirmado(confirmados);
                dto.setCantidadTotal(pendientes + confirmados);
                
                return new Object[]{diaSemana, dto};
            })
            .collect(Collectors.groupingBy(
                arr -> (String) arr[0],
                Collectors.mapping(arr -> (PlatoPedidoDTO) arr[1], Collectors.toList())
            ));
        
        // Convertir a DTOs por día con orden específico
        List<String> ordenDias = List.of("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES");
        List<PedidosPorDiaDTO> pedidosPorDia = ordenDias.stream()
            .filter(dia -> platosPorDia.containsKey(dia) && !platosPorDia.get(dia).isEmpty())
            .map(dia -> {
                PedidosPorDiaDTO diaDto = new PedidosPorDiaDTO();
                diaDto.setDiaSemana(dia);
                diaDto.setPlatos(platosPorDia.get(dia));
                diaDto.setTotalPlatosDia(platosPorDia.get(dia).stream()
                    .mapToInt(PlatoPedidoDTO::getCantidadTotal)
                    .sum());
                return diaDto;
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
        response.setPedidosPorDia(pedidosPorDia);
        response.setTotalPedidos(totalPedidos);
        response.setMensaje("Listado de platos pedidos agrupados por día obtenido exitosamente");
        
        return response;
    }
}