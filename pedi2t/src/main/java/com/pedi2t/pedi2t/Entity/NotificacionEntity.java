package com.pedi2t.pedi2t.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notificaciones")
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @NotBlank(message = "El asunto no puede estar vacío")
    @Size(min = 1, max = 100, message = "El asunto debe tener entre 1 y 100 caracteres")
    @Column(name = "asunto", nullable = false)
    private String asunto;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 1, max = 100, message = "El mensaje debe tener entre 1 y 100 caracteres")
    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "leida", nullable = false)
    private boolean leida = false;

    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    @Column(name = "tipo")
    private String tipo;

    @NotNull(message = "El pedido debe estar asociado a un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "FK_notificacion_usuarios"))
    private UsuarioEntity usuario;

}
