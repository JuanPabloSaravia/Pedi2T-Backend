package com.pedi2t.pedi2t.Service.Admin.ServiceImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoDTO;
import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoResponseDTO;
import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Enum.EstadoPedido;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.PedidoRepository;
import com.pedi2t.pedi2t.Service.Admin.ConfirmarPedidoAdminService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConfirmarPedidoAdminServiceImpl implements ConfirmarPedidoAdminService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;
    
    @Autowired
    private MenuDiaRepository menuDiaRepository;

    @Override
    public ConfirmarPedidoResponseDTO confirmarPedido(ConfirmarPedidoDTO confirmarPedidoDTO) {
        
        // Buscar todos los pedidos pendientes en el rango de fechas
        List<PedidoDia> pedidosPendientes = pedidoDiaRepository.findPedidosPendientesByFechaRange(
            confirmarPedidoDTO.getFechaInicio(), 
            confirmarPedidoDTO.getFechaFin()
        );
        
        if (pedidosPendientes.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron pedidos pendientes en el rango de fechas especificado");
        }
        
        // Confirmar todos los pedidos pendientes y contar por plato
        int cantidadConfirmados = 0;
        Map<String, Integer> resumenPedidos = new HashMap<>();
        Map<String, String> categoriasPorPlato = new HashMap<>();
        
        for (PedidoDia pedidoDia : pedidosPendientes) {
            var pedido = pedidoDia.getPedidoEntity();
            pedido.setEstado(EstadoPedido.CONFIRMADO);
            pedidoRepository.save(pedido);
            cantidadConfirmados++;
            
            // Contar pedidos por plato
            String nombrePlato = pedidoDia.getPlato().getNombre();
            String categoria = pedidoDia.getPlato().getCategoria();
            resumenPedidos.put(nombrePlato, resumenPedidos.getOrDefault(nombrePlato, 0) + 1);
            categoriasPorPlato.put(nombrePlato, categoria);
        }
        
        // Generar archivo de resumen
        String rutaArchivo = generarArchivoResumen(resumenPedidos, categoriasPorPlato, 
                                                 confirmarPedidoDTO.getFechaInicio(), 
                                                 confirmarPedidoDTO.getFechaFin());
        
        // Despublicar todos los menús que estaban publicados
        despublicarTodosLosMenus();
        
        // Crear la respuesta
        ConfirmarPedidoResponseDTO response = new ConfirmarPedidoResponseDTO();
        response.setCantidadPedidosConfirmados(cantidadConfirmados);
        response.setMensaje("Se confirmaron " + cantidadConfirmados + " pedidos exitosamente");
        response.setFechaInicio(confirmarPedidoDTO.getFechaInicio());
        response.setFechaFin(confirmarPedidoDTO.getFechaFin());
        response.setRutaArchivoResumen(rutaArchivo);
        
        return response;
    }
    
    private String generarArchivoResumen(Map<String, Integer> resumenPedidos, 
                                       Map<String, String> categoriasPorPlato,
                                       LocalDate fechaInicio, 
                                       LocalDate fechaFin) {
        try {
            // Crear directorio si no existe
            Path directorioReportes = Paths.get("reportes");
            if (!Files.exists(directorioReportes)) {
                Files.createDirectories(directorioReportes);
            }
            
            // Generar nombre del archivo con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = String.format("resumen_pedidos_%s.txt", timestamp);
            Path rutaArchivo = directorioReportes.resolve(nombreArchivo);
            
            // Generar contenido del archivo
            try (FileWriter writer = new FileWriter(rutaArchivo.toFile())) {
                writer.write("RESUMEN DE PEDIDOS CONFIRMADOS\n");
                writer.write("===============================\n\n");
                writer.write("Período: " + fechaInicio + " al " + fechaFin + "\n");
                writer.write("Fecha de generación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n\n");
                
                // Agrupar por categoría
                Map<String, Map<String, Integer>> porCategoria = new HashMap<>();
                for (Map.Entry<String, Integer> entrada : resumenPedidos.entrySet()) {
                    String plato = entrada.getKey();
                    Integer cantidad = entrada.getValue();
                    String categoria = categoriasPorPlato.get(plato);
                    
                    porCategoria.computeIfAbsent(categoria, k -> new HashMap<>()).put(plato, cantidad);
                }
                
                // Escribir por categoría
                for (Map.Entry<String, Map<String, Integer>> categoria : porCategoria.entrySet()) {
                    writer.write("CATEGORÍA: " + categoria.getKey().toUpperCase() + "\n");
                    writer.write("-".repeat(categoria.getKey().length() + 11) + "\n");
                    
                    for (Map.Entry<String, Integer> plato : categoria.getValue().entrySet()) {
                        writer.write(String.format("• %s: %d porciones\n", plato.getKey(), plato.getValue()));
                    }
                    writer.write("\n");
                }
                
                // Resumen total
                int totalPorciones = resumenPedidos.values().stream().mapToInt(Integer::intValue).sum();
                writer.write("RESUMEN TOTAL\n");
                writer.write("=============\n");
                writer.write("Total de porciones: " + totalPorciones + "\n");
                writer.write("Total de platos diferentes: " + resumenPedidos.size() + "\n");
            }
            
            return rutaArchivo.toString();
            
        } catch (IOException e) {
            throw new RuntimeException("Error al generar archivo de resumen: " + e.getMessage());
        }
    }
    
    private void despublicarTodosLosMenus() {
        // Buscar todos los menús que están publicados
        List<MenuDiaEntity> menusPublicados = menuDiaRepository.findAllByPublicadoTrue();
        
        // Marcar como no publicados
        for (MenuDiaEntity menu : menusPublicados) {
            menu.setPublicado(false);
            menuDiaRepository.save(menu);
        }
    }
}