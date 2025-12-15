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
     * Calcula la próxima fecha para un día de la semana (excluyendo hoy)
     * Siempre devuelve una fecha futura, aunque hoy sea el día solicitado
     * 
     * @param diaObjetivoStr Día de la semana en español
     * @return La próxima fecha futura para ese día
     */
    public static LocalDate calcularSiguienteFecha(String diaObjetivoStr) {
        LocalDate hoy = LocalDate.now();
        
        DayOfWeek diaObjetivo = DIAS_SEMANA_MAP.get(diaObjetivoStr.toUpperCase().trim());
        
        if (diaObjetivo == null) {
            throw new IllegalArgumentException("Día no válido: " + diaObjetivoStr + 
                ". Días válidos: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES");
        }

        // Siempre devolver la próxima ocurrencia (nunca hoy)
        return hoy.with(TemporalAdjusters.next(diaObjetivo));
    }
    
}