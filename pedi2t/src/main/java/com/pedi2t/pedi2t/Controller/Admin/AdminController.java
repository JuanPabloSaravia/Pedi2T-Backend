package com.pedi2t.pedi2t.Controller.Admin;

import com.pedi2t.pedi2t.DTO.Admin.CargarPlatosRequestDTO;
import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoDTO;
import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoResponseDTO;
import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosDTO;
import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosResponseDTO;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Service.Admin.ConfirmarPedidoAdminService;
import com.pedi2t.pedi2t.Service.Admin.EntregarPedidosAdminService;
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
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    
    @Autowired
    private PlatoAdminService platoAdminService;
    
    @Autowired
    private ConfirmarPedidoAdminService confirmarPedidoAdminService;
    
    @Autowired
    private EntregarPedidosAdminService entregarPedidosAdminService;
    
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

    @PutMapping("/confirmarPedido")
    public ResponseEntity<ConfirmarPedidoResponseDTO> confirmarPedido(@RequestBody @Valid ConfirmarPedidoDTO confirmarPedidoDTO) {
        
        try {
            ConfirmarPedidoResponseDTO response = confirmarPedidoAdminService.confirmarPedido(confirmarPedidoDTO);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(null); // En un caso real, podrías crear un DTO de error
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
    
    @PutMapping("/entregarPedidos")
    public ResponseEntity<EntregarPedidosResponseDTO> entregarPedidos(@RequestBody @Valid EntregarPedidosDTO entregarPedidosDTO) {
        
        try {
            EntregarPedidosResponseDTO response = entregarPedidosAdminService.marcarPedidosComoEntregados(entregarPedidosDTO);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
}
