package com.pedi2t.pedi2t.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "dias_presenciales")
public class DiasPresencialesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "lunes")
    private Boolean lunes = false;

    @Column(name = "martes")
    private Boolean martes = false;

    @Column(name = "miercoles")
    private Boolean miercoles = false;

    @Column(name = "jueves")
    private Boolean jueves = false;

    @Column(name = "viernes")
    private Boolean viernes = false;
}