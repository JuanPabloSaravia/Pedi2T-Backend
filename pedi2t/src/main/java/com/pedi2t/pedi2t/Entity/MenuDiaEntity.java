package com.pedi2t.pedi2t.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "menu_dia")
public class MenuDiaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "la descripcion no puede estar vacío")
    @Size(min = 1, max = 100, message = "La descripcion debe tener entre 1 y 100 caracteres")
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    
    @ColumnDefault("false")
    @Column(name = "publicado", nullable = false)
    private Boolean publicado;

    @NotNull(message = "el stock total no puede ser nulo")
    @Min(value = 1, message = "El stock total debe ser al menos 1")
    @Column(name = "stock_total", nullable = false)
    private Integer stockTotal;

    @NotNull(message = "El pedido debe estar asociado a un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_pedidos_usuarios")
    )
    private UsuarioEntity usuario;

}
