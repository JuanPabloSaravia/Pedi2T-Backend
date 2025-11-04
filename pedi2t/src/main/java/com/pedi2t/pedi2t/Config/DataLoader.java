package com.pedi2t.pedi2t.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Usuario administrador de ejemplo
            String adminEmail = "pedrito@pedi2t.local";
            if (usuarioRepo.findByEmail(adminEmail).isEmpty()) {
                UsuarioEntity admin = new UsuarioEntity();
                admin.setNombre("Pedro");
                admin.setApellido("Soria");
                admin.setEmail(adminEmail);
                // La contraseña debe cumplir las validaciones de la entidad. Aquí la encriptamos.
                admin.setContrasena(passwordEncoder.encode("ADmin123!"));
                admin.setTelefono("1234567890");
                admin.setDireccion("Sede Central");
                admin.setRol("ADMIN");

                usuarioRepo.save(admin);
                System.out.println("Usuario admin creado: " + adminEmail);
            } else {
                System.out.println("Usuario admin ya existe: " + adminEmail);
            }

            // Usuario empleado de ejemplo
            String empleadoEmail = "Joti@pedi2t.local";
            if (usuarioRepo.findByEmail(empleadoEmail).isEmpty()) {
                UsuarioEntity emp = new UsuarioEntity();
                emp.setNombre("Joti");
                emp.setApellido("Saravia");
                emp.setEmail(empleadoEmail);
                emp.setContrasena(passwordEncoder.encode("Empleado123!"));
                emp.setTelefono("0987654321");
                emp.setDireccion("Sucursal Demo");
                emp.setRol("EMPLEADO");

                usuarioRepo.save(emp);
                System.out.println("Usuario empleado creado: " + empleadoEmail);
            } else {
                System.out.println("Usuario empleado ya existe: " + empleadoEmail);
            }
        };
    }
}
