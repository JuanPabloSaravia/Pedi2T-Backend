package com.pedi2t.pedi2t.Service.Admin;

import com.pedi2t.pedi2t.DTO.Admin.CargarPlatosRequestDTO;
import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.MenuPlatosRepository;
import com.pedi2t.pedi2t.Repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PlatoAdminService {
    
    @Autowired
    private PlatoRepository platoRepository;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @Autowired
    private MenuDiaRepository menuDiaRepository;
    
    @Autowired
    private MenuPlatosRepository menuPlatosRepository;
    
    @Autowired
    private com.pedi2t.pedi2t.Repository.UsuarioRepository usuarioRepository;
    
    /**
     * Carga un nuevo plato con imagen subida a Cloudinary y lo asocia a los días especificados
     * @param platoDTO Datos del plato incluyendo días de la semana
     * @param imagenFile Archivo de imagen
     * @return Plato guardado en la base de datos
     * @throws IOException Si hay error subiendo la imagen
     */
    public PlatoEntity cargarPlato(CargarPlatosRequestDTO platoDTO, MultipartFile imagenFile) throws IOException {
        // Validar días de la semana
        if (!sonDiasValidos(platoDTO.getDiasSemana())) {
            throw new IllegalArgumentException("Uno o más días de la semana no son válidos. Días permitidos: lunes, martes, miercoles, jueves, viernes");
        }
        
        // Subir imagen a Cloudinary
        String imageUrl = cloudinaryService.uploadImage(imagenFile, "pedi2t/platos");
        
        // Crear entidad del plato
        PlatoEntity plato = new PlatoEntity();
        plato.setNombre(platoDTO.getNombre());
        plato.setDescripcion(platoDTO.getDescripcion());
        plato.setCategoria(platoDTO.getCategoria());
        plato.setImagenUrl(imageUrl);
        
        // Guardar el plato en la base de datos
        PlatoEntity platoGuardado = platoRepository.save(plato);
        
        // Asociar el plato a los días de la semana especificados
        asociarPlatoADias(platoGuardado, platoDTO.getDiasSemana());
        
        return platoGuardado;
    }
    
    /**
     * Valida que todos los días en la lista sean válidos
     * @param diasSemana Lista de días de la semana
     * @return true si todos los días son válidos
     */
    private boolean sonDiasValidos(List<String> diasSemana) {
        if (diasSemana == null || diasSemana.isEmpty()) {
            return false;
        }
        
        List<String> diasPermitidos = List.of("lunes", "martes", "miercoles", "miércoles", "jueves", "viernes");
        
        return diasSemana.stream()
            .allMatch(dia -> dia != null && diasPermitidos.contains(dia.trim().toLowerCase()));
    }
    
    /**
     * Asocia un plato a los días de la semana especificados
     * @param plato Plato a asociar
     * @param diasString Lista de días como strings
     */
    private void asociarPlatoADias(PlatoEntity plato, List<String> diasString) {
        for (String diaStr : diasString) {
            String diaMayuscula = convertirAMayuscula(diaStr.trim().toLowerCase());
            
            // Buscar o crear MenuDia para este día
            MenuDiaEntity menuDia = menuDiaRepository.findByDiaSemana(diaMayuscula)
                    .orElseGet(() -> crearMenuDiaPorDefecto(diaMayuscula));
            
            // Crear la asociación MenuPlatos
            MenuPlatosEntity menuPlatos = new MenuPlatosEntity();
            menuPlatos.setPlato(plato);
            menuPlatos.setMenuDia(menuDia);
            
            menuPlatosRepository.save(menuPlatos);
        }
    }
    
    /**
     * Convierte el día de minúsculas a mayúsculas según el formato requerido por la entidad
     * @param diaMinuscula Día en minúsculas
     * @return Día en mayúsculas
     */
    private String convertirAMayuscula(String diaMinuscula) {
        return switch (diaMinuscula) {
            case "lunes" -> "LUNES";
            case "martes" -> "MARTES";
            case "miercoles", "miércoles" -> "MIERCOLES";
            case "jueves" -> "JUEVES";
            case "viernes" -> "VIERNES";
            default -> diaMinuscula.toUpperCase();
        };
    }
    
    /**
     * Crea un MenuDia por defecto para el día especificado
     * @param diaSemana Día de la semana como string
     * @return MenuDia creado
     */
    private MenuDiaEntity crearMenuDiaPorDefecto(String diaSemana) {
        // Buscar usuario admin
        Optional<com.pedi2t.pedi2t.Entity.UsuarioEntity> usuarioAdminOpt = usuarioRepository.findByRol("ADMIN");
        if (usuarioAdminOpt.isEmpty()) {
            throw new RuntimeException("No se encontró usuario administrador. Asegúrate de tener un usuario con rol ADMIN");
        }
        
        com.pedi2t.pedi2t.Entity.UsuarioEntity usuarioAdmin = usuarioAdminOpt.get();
        
        MenuDiaEntity menuDia = new MenuDiaEntity();
        menuDia.setDiaSemana(diaSemana); // Ya debe estar en MAYÚSCULAS
        menuDia.setDescripcion("Menú del " + diaSemana.toLowerCase());
        menuDia.setPublicado(Boolean.TRUE); // Explícitamente TRUE
        menuDia.setStockTotal(100); // Stock por defecto
        menuDia.setUsuario(usuarioAdmin);
        
        return menuDiaRepository.save(menuDia);
    }
    
    /**
     * Actualiza un plato existente, opcionalmente con nueva imagen
     * @param id ID del plato a actualizar
     * @param platoDTO Nuevos datos del plato
     * @param imagenFile Nueva imagen (opcional)
     * @return Plato actualizado
     * @throws IOException Si hay error subiendo la imagen
     * @throws RuntimeException Si el plato no existe
     */
    public PlatoEntity actualizarPlato(Long id, CargarPlatosRequestDTO platoDTO, MultipartFile imagenFile) throws IOException {
        if (id == null) {
            throw new IllegalArgumentException("El ID del plato no puede ser nulo");
        }
        
        PlatoEntity plato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + id));
        
        // Si se proporciona nueva imagen, subir a Cloudinary y eliminar la anterior
        if (imagenFile != null && !imagenFile.isEmpty()) {
            // Eliminar imagen anterior de Cloudinary
            String oldPublicId = cloudinaryService.extractPublicId(plato.getImagenUrl());
            if (oldPublicId != null) {
                cloudinaryService.deleteImage(oldPublicId);
            }
            
            // Subir nueva imagen
            String newImageUrl = cloudinaryService.uploadImage(imagenFile, "pedi2t/platos");
            plato.setImagenUrl(newImageUrl);
        }
        
        // Actualizar datos del plato
        plato.setNombre(platoDTO.getNombre());
        plato.setDescripcion(platoDTO.getDescripcion());
        plato.setCategoria(platoDTO.getCategoria());
        
        return platoRepository.save(plato);
    }
    
    /**
     * Elimina un plato y su imagen de Cloudinary
     * @param id ID del plato a eliminar
     * @throws RuntimeException Si el plato no existe
     */
    public void eliminarPlato(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del plato no puede ser nulo");
        }
        
        PlatoEntity plato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + id));
        
        // Eliminar imagen de Cloudinary
        String publicId = cloudinaryService.extractPublicId(plato.getImagenUrl());
        if (publicId != null) {
            cloudinaryService.deleteImage(publicId);
        }
        
        // Eliminar plato de la base de datos
        platoRepository.delete(plato);
    }
}