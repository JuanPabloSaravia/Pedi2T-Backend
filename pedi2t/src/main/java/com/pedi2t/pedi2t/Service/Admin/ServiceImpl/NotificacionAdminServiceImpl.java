package com.pedi2t.pedi2t.Service.Admin.ServiceImpl;

import com.pedi2t.pedi2t.DTO.Admin.UsuarioSinSeleccionDTO;
import com.pedi2t.pedi2t.Entity.NotificacionEntity;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.NotificacionRepository;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.Admin.NotificacionAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificacionAdminServiceImpl implements NotificacionAdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Override
    public List<UsuarioSinSeleccionDTO> obtenerUsuariosSinSeleccion() {
        List<UsuarioEntity> todos = usuarioRepository.findAll();

        List<UsuarioEntity> empleados = todos.stream()
                .filter(u -> "EMPLEADO".equals(u.getRol()))
                .collect(Collectors.toList());

        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusDays(7);

        List<PedidoDia> pedidosPendientes = pedidoDiaRepository.findPedidosPendientesByFechaRange(inicio, fin);

        Set<Long> usuariosConPedidos = pedidosPendientes.stream()
                .map(pd -> pd.getPedidoEntity().getUsuario().getId())
                .collect(Collectors.toSet());

        return empleados.stream()
                .filter(u -> !usuariosConPedidos.contains(u.getId()))
                .map(u -> {
                    UsuarioSinSeleccionDTO dto = new UsuarioSinSeleccionDTO();
                    dto.setId(u.getId());
                    dto.setUsername(u.getEmail());
                    dto.setEmail(u.getEmail());
                    dto.setNombre(u.getNombre());
                    dto.setApellido(u.getApellido());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void notificarUsuario(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        NotificacionEntity notificacion = new NotificacionEntity();
        notificacion.setAsunto("Recordatorio de pedido");
        notificacion.setMensaje(
                "Hola " + usuario.getNombre() + ", todavía no realizaste tu pedido. Por favor selecciona tu plato.");
        notificacion.setUsuario(usuario);

        notificacionRepository.save(notificacion);
    }

    @Override
    public void notificarTodos() {
        List<UsuarioSinSeleccionDTO> sinSeleccion = obtenerUsuariosSinSeleccion();

        List<Long> ids = sinSeleccion.stream().map(UsuarioSinSeleccionDTO::getId).collect(Collectors.toList());

        List<UsuarioEntity> usuarios = usuarioRepository.findAllById(ids);

        List<NotificacionEntity> notis = usuarios.stream().map(u -> {
            NotificacionEntity n = new NotificacionEntity();
            n.setAsunto("Recordatorio de pedido");
            n.setMensaje("Hola " + u.getNombre() + ", todavía no realizaste tu pedido. Por favor selecciona tu plato.");
            n.setUsuario(u);
            return n;
        }).collect(Collectors.toList());

        notificacionRepository.saveAll(notis);
    }
}
