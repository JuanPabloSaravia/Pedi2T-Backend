package com.pedi2t.pedi2t.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.Service.UsuarioService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrarUsuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid UsuarioRegistroDTO usuarioDTO, BindingResult result) {
        if (result.hasErrors()) {
            // devolver lista de errores 
            List<String> errores = result.getFieldErrors() 
                    .stream() 
                    .map(err -> err.getField() + ": " + err.getDefaultMessage()) 
                    .toList(); 
            return ResponseEntity.badRequest().body(errores); 
        }
        if (usuarioDTO == null) {
            return ResponseEntity.badRequest().body("El usuario no puede ser nulo");
        }
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuarioDTO));
    }
    
}
