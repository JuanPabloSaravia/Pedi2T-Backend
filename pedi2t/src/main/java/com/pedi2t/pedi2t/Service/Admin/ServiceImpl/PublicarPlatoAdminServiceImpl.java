package com.pedi2t.pedi2t.Service.Admin.ServiceImpl;

import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.MenuPlatosRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.Admin.PublicarPlatoAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicarPlatoAdminServiceImpl implements PublicarPlatoAdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MenuPlatosRepository menuPlatosRepository;
    
    @Override
    public void republicarPlato(Long usuarioId, Long menuPlatoId) {
        // Verificar que el usuario sea admin
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
        if (!"ADMIN".equals(usuario.getRol())) {
            throw new SecurityException("Solo los administradores pueden republicar platos");
        }
        
        // Buscar el MenuPlatosEntity específico por su ID
        MenuPlatosEntity menuPlato = menuPlatosRepository.findById(menuPlatoId)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el menú-plato con ID: " + menuPlatoId));
        
        menuPlato.setPublicado(true);
        menuPlatosRepository.save(menuPlato);
    }
    
    @Override
    public void republicarTodosLosPlatos(Long usuarioId) {
        // Verificar que el usuario sea admin
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
        if (!"ADMIN".equals(usuario.getRol())) {
            throw new SecurityException("Solo los administradores pueden republicar platos");
        }
        
        // Buscar todos los MenuPlatosEntity despublicados y republicarlos
        List<MenuPlatosEntity> menuPlatos = menuPlatosRepository.findAll().stream()
            .filter(mp -> !mp.getPublicado())
            .collect(Collectors.toList());
        
        for (MenuPlatosEntity menuPlato : menuPlatos) {
            menuPlato.setPublicado(true);
        }
        
        menuPlatosRepository.saveAll(menuPlatos);
    }
    
    @Override
    public void despublicarPlato(Long usuarioId, Long menuPlatoId) {
        // Verificar que el usuario sea admin
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
        if (!"ADMIN".equals(usuario.getRol())) {
            throw new SecurityException("Solo los administradores pueden despublicar platos");
        }
        
        // Buscar el MenuPlatosEntity específico por su ID
        MenuPlatosEntity menuPlato = menuPlatosRepository.findById(menuPlatoId)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el menú-plato con ID: " + menuPlatoId));
        
        menuPlato.setPublicado(false);
        menuPlatosRepository.save(menuPlato);
    }
    
    @Override
    public void despublicarTodosLosPlatos(Long usuarioId) {
        // Verificar que el usuario sea admin
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
        if (!"ADMIN".equals(usuario.getRol())) {
            throw new SecurityException("Solo los administradores pueden despublicar platos");
        }
        
        // Buscar todos los MenuPlatosEntity publicados y despublicarlos
        List<MenuPlatosEntity> menuPlatos = menuPlatosRepository.findByPublicadoTrue();
        
        for (MenuPlatosEntity menuPlato : menuPlatos) {
            menuPlato.setPublicado(false);
        }
        
        menuPlatosRepository.saveAll(menuPlatos);
    }
}