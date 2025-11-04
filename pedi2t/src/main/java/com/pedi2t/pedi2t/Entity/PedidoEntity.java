package com.pedi2t.pedi2t.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "pedidos")
public class PedidoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 50, message = "El estado no puede exceder los 50 caracteres")
    @Column(name = "estado", nullable = false)
    private String estado;

    @NotNull(message = "La cantidad de personas no puede ser nula")
    @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
    @Column(name = "cantidad persona", nullable = false)
    private Integer cantidadPersona;

    @NotNull(message = "El pedido debe estar asociado a un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_pedido_usuario")
    )
    private UsuarioEntity usuario;
    
}