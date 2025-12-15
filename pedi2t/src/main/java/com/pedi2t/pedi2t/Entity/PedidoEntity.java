package com.pedi2t.pedi2t.Entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.pedi2t.pedi2t.Enum.EstadoPedido;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "pedidos")
public class PedidoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El estado del pedido es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPedido estado;


    @NotNull(message = "El pedido debe estar asociado a un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_pedido_usuario")
    )
    private UsuarioEntity usuario;

    @CreationTimestamp
    @Column(name = "fecha entrega", nullable = false, updatable = false)
    private LocalDateTime fechaPedido;
    
}