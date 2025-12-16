package com.pedi2t.pedi2t.Service.ServiceImpl;

import com.pedi2t.pedi2t.DTO.NotificacionResponseDTO;
import com.pedi2t.pedi2t.Entity.NotificacionEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.NotificacionRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<NotificacionResponseDTO> obtenerNotificacionesPorUsuario(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        List<NotificacionEntity> notis = notificacionRepository.findAll().stream()
                .filter(n -> n.getUsuario().getId().equals(usuario.getId()))
                .sorted((a, b) -> b.getFechaEnvio().compareTo(a.getFechaEnvio()))
                .collect(Collectors.toList());

        return notis.stream().map(n -> {
            NotificacionResponseDTO dto = new NotificacionResponseDTO();
            dto.setId(n.getId());
            dto.setTitulo(n.getAsunto());
            dto.setMensaje(n.getMensaje());
            dto.setFecha(n.getFechaEnvio());
            dto.setLeida(n.isLeida());
            dto.setTipo(n.getTipo());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void marcarComoLeida(Long notificacionId) {
        NotificacionEntity n = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada: " + notificacionId));
        if (!n.isLeida()) {
            n.setLeida(true);
            notificacionRepository.save(n);
        }
    }

    @Override
    public void eliminarNotificacion(Long notificacionId) {
        if (!notificacionRepository.existsById(notificacionId)) {
            throw new IllegalArgumentException("Notificación no encontrada: " + notificacionId);
        }
        notificacionRepository.deleteById(notificacionId);
    }
}
