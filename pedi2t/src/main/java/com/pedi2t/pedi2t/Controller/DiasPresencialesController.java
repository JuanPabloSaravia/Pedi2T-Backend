package com.pedi2t.pedi2t.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pedi2t.pedi2t.DTO.DiasPresencialesDTO;
import com.pedi2t.pedi2t.Service.DiasPresencialesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/dias-presenciales")
public class DiasPresencialesController {

    @Autowired
    private DiasPresencialesService diasPresencialesService;

    @PutMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> actualizarDiasPresenciales(
            @PathVariable Long usuarioId,
            @Valid @RequestBody DiasPresencialesDTO diasDTO) {
        try {
            return ResponseEntity.ok(diasPresencialesService.actualizarDiasPresenciales(usuarioId, diasDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerDiasPresenciales(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(diasPresencialesService.obtenerDiasPresenciales(usuarioId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}