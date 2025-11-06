package com.pedi2t.pedi2t.Service.Admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    
    private final Cloudinary cloudinary;
    
    public CloudinaryService(
            @Value("${CLOUDINARY_CLOUD_NAME:#{null}}") String cloudName,
            @Value("${CLOUDINARY_API_KEY:#{null}}") String apiKey,
            @Value("${CLOUDINARY_API_SECRET:#{null}}") String apiSecret,
            @Value("${CLOUDINARY_URL:#{null}}") String cloudinaryUrl) {
        
        // Priorizar CLOUDINARY_URL si está disponible
        if (cloudinaryUrl != null && !cloudinaryUrl.trim().isEmpty()) {
            cloudinary = new Cloudinary(cloudinaryUrl);
            System.out.println("Cloudinary configurado usando CLOUDINARY_URL");
        } else if (cloudName != null && apiKey != null && apiSecret != null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
            ));
            System.out.println("Cloudinary configurado usando variables individuales");
        } else {
            throw new IllegalStateException(
                "Configuración de Cloudinary faltante. " +
                "Proporciona CLOUDINARY_URL o las variables CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY y CLOUDINARY_API_SECRET"
            );
        }
    }
    
    /**
     * Sube una imagen a Cloudinary y retorna la URL pública
     * @param file Archivo de imagen a subir
     * @param folder Carpeta donde guardar la imagen en Cloudinary (opcional)
     * @return URL pública de la imagen subida
     * @throws IOException Si hay error en la subida
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        // Validar que sea un archivo de imagen
        if (!isImageFile(file)) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }
        
        try {
            // Configurar parámetros de subida (versión simplificada)
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "resource_type", "image",
                "folder", folder != null ? folder : "pedi2t/platos",
                "use_filename", true,
                "unique_filename", true,
                "overwrite", false
            );
            
            // Subir archivo
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            
            // Retornar URL segura (HTTPS)
            return (String) uploadResult.get("secure_url");
            
        } catch (Exception e) {
            throw new IOException("Error al subir imagen a Cloudinary: " + e.getMessage(), e);
        }
    }
    
    /**
     * Elimina una imagen de Cloudinary usando su public_id
     * @param publicId ID público de la imagen en Cloudinary
     * @return true si se eliminó correctamente
     */
    public boolean deleteImage(String publicId) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Extrae el public_id de una URL de Cloudinary
     * @param imageUrl URL completa de la imagen
     * @return public_id de la imagen
     */
    public String extractPublicId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }
        
        // Ejemplo URL: https://res.cloudinary.com/cloud_name/image/upload/v1234567890/folder/image_name.jpg
        // Public ID sería: folder/image_name
        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length == 2) {
                String afterUpload = parts[1];
                // Remover la versión (v1234567890) si existe
                if (afterUpload.startsWith("v") && afterUpload.contains("/")) {
                    afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
                }
                // Remover la extensión
                int dotIndex = afterUpload.lastIndexOf(".");
                if (dotIndex > 0) {
                    afterUpload = afterUpload.substring(0, dotIndex);
                }
                return afterUpload;
            }
        } catch (Exception e) {
            // Si hay error, retornar null
        }
        return null;
    }
    
    /**
     * Verifica si el archivo es una imagen válida
     * @param file Archivo a verificar
     * @return true si es una imagen
     */
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}