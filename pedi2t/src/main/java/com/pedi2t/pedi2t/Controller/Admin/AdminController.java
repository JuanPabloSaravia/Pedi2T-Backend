package com.pedi2t.pedi2t.Controller.Admin;

import com.pedi2t.pedi2t.DTO.Admin.CargarPlatosRequestDTO;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Service.Admin.PlatoAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private PlatoAdminService platoAdminService;
    
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
}
