package com.pedi2t.pedi2t.Controller.User;

import com.pedi2t.pedi2t.DTO.NotificacionResponseDTO;
import com.pedi2t.pedi2t.Service.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/{usuarioId}/notificaciones")
    public ResponseEntity<List<NotificacionResponseDTO>> getNotificaciones(@PathVariable Long usuarioId) {
        try {
            List<NotificacionResponseDTO> lista = notificacionService.obtenerNotificacionesPorUsuario(usuarioId);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/notificaciones/{id}/marcar-leida")
    public ResponseEntity<?> marcarLeida(@PathVariable Long id) {
        try {
            notificacionService.marcarComoLeida(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/notificaciones/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            notificacionService.eliminarNotificacion(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
