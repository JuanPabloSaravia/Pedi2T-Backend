package com.pedi2t.pedi2t.Service.Admin;

import com.pedi2t.pedi2t.DTO.Admin.NotificarRequestDTO;
import com.pedi2t.pedi2t.Entity.NotificacionEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Repository.NotificacionRepository;
import com.pedi2t.pedi2t.Repository.PedidoRepository;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;

    /**
     * Obtiene todos los usuarios EMPLEADO que NO han seleccionado pedidos para la semana siguiente
     * Busca por PedidoDia con fechaEntrega en la próxima semana
     */
    public List<UsuarioEntity> obtenerUsuariosSinPedidos() {
        // Obtener todos los usuarios con rol EMPLEADO
        List<UsuarioEntity> empleados = usuarioRepository.findAll().stream()
            .filter(u -> u.getRol().equals("EMPLEADO"))
            .collect(Collectors.toList());
        
        // Calcular rango de fechas de la próxima semana (lunes a viernes)
        LocalDate hoy = LocalDate.now();
        LocalDate proximoLunes = hoy.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate proximoViernes = proximoLunes.plusDays(4); // Lunes + 4 días = Viernes
        
        // Obtener todos los PedidoDia con fechaEntrega en la próxima semana
        List<PedidoDia> pedidosProximaSemana = pedidoDiaRepository.findAll().stream()
            .filter(pd -> {
                LocalDate fecha = pd.getFechaEntrega();
                return !fecha.isBefore(proximoLunes) && !fecha.isAfter(proximoViernes);
            })
            .collect(Collectors.toList());
        
        // Obtener el conjunto de IDs de usuarios que sí tienen pedidos para la próxima semana
        Set<Long> usuariosConPedidos = pedidosProximaSemana.stream()
            .map(pd -> pd.getPedidoEntity().getUsuario().getId())
            .collect(Collectors.toSet());
        
        // Retornar usuarios que NO tienen pedidos en la próxima semana
        return empleados.stream()
            .filter(usuario -> !usuariosConPedidos.contains(usuario.getId()))
            .collect(Collectors.toList());
    }

    /**
     * Envía notificación a un usuario individual
     */
    public NotificacionEntity notificarUsuario(NotificarRequestDTO request) {
        UsuarioEntity usuario = usuarioRepository.findById(request.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        NotificacionEntity notificacion = new NotificacionEntity();
        notificacion.setAsunto(request.getAsunto());
        notificacion.setMensaje(request.getMensaje());
        notificacion.setUsuario(usuario);

        return notificacionRepository.save(notificacion);
    }

    /**
     * Envía notificación a todos los usuarios sin pedido
     */
    public void notificarATodos(String asunto, String mensaje) {
        List<UsuarioEntity> usuariosSinPedido = obtenerUsuariosSinPedidos();
        
        usuariosSinPedido.forEach(usuario -> {
            NotificacionEntity notificacion = new NotificacionEntity();
            notificacion.setAsunto(asunto);
            notificacion.setMensaje(mensaje);
            notificacion.setUsuario(usuario);
            notificacionRepository.save(notificacion);
        });
    }
}
