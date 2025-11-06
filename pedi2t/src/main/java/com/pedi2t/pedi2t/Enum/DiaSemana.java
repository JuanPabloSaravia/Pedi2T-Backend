package com.pedi2t.pedi2t.Enum;

public enum DiaSemana {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes");
    
    private final String nombre;
    
    DiaSemana(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Convierte un string a enum, case-insensitive
     */
    public static DiaSemana fromString(String dia) {
        for (DiaSemana d : DiaSemana.values()) {
            if (d.name().equalsIgnoreCase(dia) || d.nombre.equalsIgnoreCase(dia)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Día no válido: " + dia + ". Días permitidos: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES");
    }
    
    /**
     * Valida si una lista de días es válida
     */
    public static boolean sonDiasValidos(java.util.List<String> dias) {
        try {
            for (String dia : dias) {
                fromString(dia);
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}