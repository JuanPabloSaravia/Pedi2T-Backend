package com.pedi2t.pedi2t.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotEnvConfig {
    
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }
    
    /**
     * Asegurar que las variables del .env estén disponibles como propiedades del sistema
     */
    public DotEnvConfig() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            
            // Cargar variables críticas de Cloudinary
            loadEnvVar(dotenv, "CLOUDINARY_CLOUD_NAME");
            loadEnvVar(dotenv, "CLOUDINARY_API_KEY");
            loadEnvVar(dotenv, "CLOUDINARY_API_SECRET");
            loadEnvVar(dotenv, "CLOUDINARY_URL");
            
            System.out.println("Variables de entorno de Cloudinary cargadas correctamente");
            
        } catch (Exception e) {
            System.err.println("Error cargando archivo .env: " + e.getMessage());
        }
    }
    
    private void loadEnvVar(Dotenv dotenv, String varName) {
        String value = dotenv.get(varName);
        if (value != null && !value.trim().isEmpty()) {
            System.setProperty(varName, value);
            System.out.println(varName + " = " + (varName.contains("SECRET") ? "***" : value));
        }
    }
}