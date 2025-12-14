package com.pedi2t.pedi2t.Service.Admin;

import com.pedi2t.pedi2t.DTO.Admin.NotificarRequestDTO;
import com.pedi2t.pedi2t.Entity.NotificacionEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.NotificacionRepository;
import com.pedi2t.pedi2t.Repository.PedidoRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Obtiene todos los usuarios EMPLEADO que no han seleccionado pedidos
     */
    public List<UsuarioEntity> obtenerUsuariosSinPedidos() {
        List<UsuarioEntity> empleados = usuarioRepository.findAll().stream()
            .filter(u -> u.getRol().equals("EMPLEADO"))
            .collect(Collectors.toList());
        
        return empleados.stream()
            .filter(usuario -> pedidoRepository.findByUsuarioId(usuario.getId()).isEmpty())
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
