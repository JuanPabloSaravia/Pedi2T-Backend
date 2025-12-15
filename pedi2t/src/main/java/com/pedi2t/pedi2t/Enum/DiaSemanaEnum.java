package com.pedi2t.pedi2t.Enum;

import java.time.DayOfWeek;

public enum DiaSemanaEnum {
    LUNES("lunes"),
    MARTES("martes"),
    MIERCOLES("miercoles"),
    JUEVES("jueves"),
    VIERNES("viernes"),
    SABADO("sabado"),
    DOMINGO("domingo");
    
    private final String nombre;
    
    DiaSemanaEnum(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public static DiaSemanaEnum fromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return LUNES;
            case TUESDAY: return MARTES;
            case WEDNESDAY: return MIERCOLES;
            case THURSDAY: return JUEVES;
            case FRIDAY: return VIERNES;
            case SATURDAY: return SABADO;
            case SUNDAY: return DOMINGO;
            default: throw new IllegalArgumentException("Día de la semana no válido: " + dayOfWeek);
        }
    }
}