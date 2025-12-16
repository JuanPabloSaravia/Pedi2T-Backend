package com.pedi2t.pedi2t.Controller.Admin;

import com.pedi2t.pedi2t.DTO.Admin.CargarPlatosRequestDTO;
import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoDTO;
import com.pedi2t.pedi2t.DTO.Admin.ConfirmarPedidoResponseDTO;
import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosDTO;
import com.pedi2t.pedi2t.DTO.Admin.EntregarPedidosResponseDTO;
import com.pedi2t.pedi2t.DTO.Admin.HomeAdminResponseDTO;
import com.pedi2t.pedi2t.DTO.Admin.PlatosPedidosResponseDTO;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Service.Admin.ConfirmarPedidoAdminService;
import com.pedi2t.pedi2t.Service.Admin.EntregarPedidosAdminService;
import com.pedi2t.pedi2t.Service.Admin.HomeAdminService;
import com.pedi2t.pedi2t.Service.Admin.PlatoAdminService;
import com.pedi2t.pedi2t.Service.Admin.PlatosPedidosAdminService;
import com.pedi2t.pedi2t.Service.Admin.PublicarPlatoAdminService;

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
    
    @Autowired
    private HomeAdminService homeAdminService;
    
    @Autowired
    private PlatosPedidosAdminService platosPedidosAdminService;
    
    @Autowired
    private PublicarPlatoAdminService publicarPlatoAdminService;
    
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
    
    @GetMapping("/home/{usuarioId}")
    public ResponseEntity<HomeAdminResponseDTO> getHomeAdmin(@PathVariable Long usuarioId) {
        try {
            HomeAdminResponseDTO response = homeAdminService.obtenerMenusParaAdmin(usuarioId);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/platosPedidos")
    public ResponseEntity<PlatosPedidosResponseDTO> obtenerPlatosPedidos() {
        try {
            PlatosPedidosResponseDTO response = platosPedidosAdminService.obtenerPlatosPedidos();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/republicarPlato/{usuarioId}/{menuPlatoId}")
    public ResponseEntity<?> republicarPlato(@PathVariable Long usuarioId, @PathVariable Long menuPlatoId) {
        try {
            publicarPlatoAdminService.republicarPlato(usuarioId, menuPlatoId);
            return ResponseEntity.ok()
                .body("Plato republicado exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("No tiene permisos para esta acción");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
        }
    }
    
    @PutMapping("/republicarTodos/{usuarioId}")
    public ResponseEntity<?> republicarTodosLosPlatos(@PathVariable Long usuarioId) {
        try {
            publicarPlatoAdminService.republicarTodosLosPlatos(usuarioId);
            return ResponseEntity.ok()
                .body("Todos los platos han sido republicados exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("No tiene permisos para esta acción");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
        }
    }
    
    @PutMapping("/despublicarPlato/{usuarioId}/{menuPlatoId}")
    public ResponseEntity<?> despublicarPlato(@PathVariable Long usuarioId, @PathVariable Long menuPlatoId) {
        try {
            publicarPlatoAdminService.despublicarPlato(usuarioId, menuPlatoId);
            return ResponseEntity.ok()
                .body("Plato despublicado exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("No tiene permisos para esta acción");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
        }
    }
    
    @PutMapping("/despublicarTodos/{usuarioId}")
    public ResponseEntity<?> despublicarTodosLosPlatos(@PathVariable Long usuarioId) {
        try {
            publicarPlatoAdminService.despublicarTodosLosPlatos(usuarioId);
            return ResponseEntity.ok()
                .body("Todos los platos han sido despublicados exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("No tiene permisos para esta acción");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
        }
    }
}
