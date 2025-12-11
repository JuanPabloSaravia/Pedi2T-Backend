package com.pedi2t.pedi2t.Controller.Admin;

import com.pedi2t.pedi2t.DTO.Admin.CargarPlatosRequestDTO;
import com.pedi2t.pedi2t.DTO.Admin.NotificarRequestDTO;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Service.Admin.PlatoAdminService;
import com.pedi2t.pedi2t.Service.Admin.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private PlatoAdminService platoAdminService;
    
    @Autowired
    private NotificacionService notificacionService;
    
    @PostMapping("/cargarPlatos")
    public ResponseEntity<?> cargarPlatos(
            @RequestPart("plato") @Valid CargarPlatosRequestDTO platoDTO,
            @RequestPart("imagen") MultipartFile imagen) {
        
        try {
            // Validar que se haya enviado una imagen
            if (imagen.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Debe proporcionar una imagen");
            }
            
            // Cargar plato con imagen en Cloudinary
            PlatoEntity platoGuardado = platoAdminService.cargarPlato(platoDTO, imagen);
            
            return ResponseEntity.ok()
                    .body("Plato cargado exitosamente. ID: " + platoGuardado.getId() + 
                          ", URL de imagen: " + platoGuardado.getImagenUrl());
                
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al subir la imagen: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }
    
    @PutMapping("/actualizarPlato/{id}")
    public ResponseEntity<?> actualizarPlato(
            @PathVariable Long id,
            @RequestPart("plato") @Valid CargarPlatosRequestDTO platoDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        
        try {
            PlatoEntity platoActualizado = platoAdminService.actualizarPlato(id, platoDTO, imagen);
            
            return ResponseEntity.ok()
                    .body("Plato actualizado exitosamente. ID: " + platoActualizado.getId() + 
                          ", URL de imagen: " + platoActualizado.getImagenUrl());
                
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al subir la imagen: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/eliminarPlato/{id}")
    public ResponseEntity<?> eliminarPlato(@PathVariable Long id) {
        try {
            platoAdminService.eliminarPlato(id);
            return ResponseEntity.ok()
                    .body("Plato eliminado exitosamente");
                    
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping("/usuariosSinSeleccion")
    public ResponseEntity<?> obtenerUsuariosSinSeleccion() {
        try {
            List<UsuarioEntity> usuarios = notificacionService.obtenerUsuariosSinPedidos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/notificarUsuario")
    public ResponseEntity<?> notificarUsuario(@Valid @RequestBody NotificarRequestDTO request) {
        try {
            notificacionService.notificarUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Notificación enviada al usuario");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/notificarATodos")
    public ResponseEntity<?> notificarATodos(
            @RequestParam String asunto,
            @RequestParam String mensaje) {
        try {
            notificacionService.notificarATodos(asunto, mensaje);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Notificaciones enviadas a todos los usuarios sin pedido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
}
