package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

public class FechaUtil {
    
    // Mapeo de días en español a DayOfWeek de Java
    private static final Map<String, DayOfWeek> DIAS_SEMANA_MAP = Map.of(
        "LUNES", DayOfWeek.MONDAY,
        "MARTES", DayOfWeek.TUESDAY,
        "MIERCOLES", DayOfWeek.WEDNESDAY,
        "JUEVES", DayOfWeek.THURSDAY,
        "VIERNES", DayOfWeek.FRIDAY
    );

    
    /**
     * Calcula la fecha en la PRÓXIMA SEMANA para un día específico
     * Siempre devuelve una fecha de la próxima semana, sin importar qué día es hoy
     * 
     * @param diaObjetivoStr Día de la semana en español  
     * @return La fecha en la próxima semana para ese día
     */
    public static LocalDate calcularSiguienteFecha(String diaObjetivoStr) {
        LocalDate hoy = LocalDate.now();
        
        DayOfWeek diaObjetivo = DIAS_SEMANA_MAP.get(diaObjetivoStr.toUpperCase().trim());
        
        if (diaObjetivo == null) {
            throw new IllegalArgumentException("Día no válido: " + diaObjetivoStr + 
                ". Días válidos: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES");
        }

        // Obtener el lunes de la próxima semana
        LocalDate proximoLunes = hoy.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        
        // Calcular el día objetivo en la próxima semana
        LocalDate fechaObjetivo = proximoLunes.with(TemporalAdjusters.nextOrSame(diaObjetivo));
        
        return fechaObjetivo;
    }
    
}