package com.pedi2t.pedi2t.Controller.User;


import com.pedi2t.pedi2t.DTO.HomeMenusResponseDTO;
import com.pedi2t.pedi2t.Service.User.HomeMenuService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/home")
@CrossOrigin(origins = "http://localhost:5173")
public class HomeController {

    @Autowired
    private HomeMenuService homeMenuService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<HomeMenusResponseDTO> getHomeUpdates(@PathVariable Long usuarioId) {
        try {
            HomeMenusResponseDTO response = homeMenuService.obtenerMenusUsuario(usuarioId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
