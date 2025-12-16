-- Script de debug para el problema de menús vacíos en home
-- Ejecutar paso a paso para diagnosticar el problema

-- 1. Verificar si el usuario existe
SELECT * FROM usuarios WHERE id = 16;

-- 2. Verificar si el usuario tiene días presenciales configurados
SELECT * FROM dias_presenciales WHERE usuario_id = 16;

-- 3. Verificar si hay menús publicados
SELECT id, fecha, descripcion, publicado, dia_semana, stock_total, id_usuario 
FROM menu_dia 
WHERE publicado = true;

-- 4. Verificar si hay platos en menu_platos publicados
SELECT mp.id, mp.publicado, mp.id_menu_dia, md.dia_semana, md.descripcion as menu_descripcion
FROM menu_platos mp
INNER JOIN menu_dia md ON mp.id_menu_dia = md.id
WHERE mp.publicado = true;

-- 5. Query completa que simula la lógica del servicio
-- Nota: Ajustar los días según lo que tenga configurado el usuario
SELECT DISTINCT
    md.id as menu_id,
    md.fecha,
    md.descripcion as menu_descripcion,
    md.dia_semana,
    md.publicado as menu_publicado,
    dp.lunes,
    dp.martes,
    dp.miercoles,
    dp.jueves,
    dp.viernes,
    COUNT(mp.id) as cantidad_platos_publicados
FROM menu_dia md
INNER JOIN dias_presenciales dp ON dp.usuario_id = 16
LEFT JOIN menu_platos mp ON mp.id_menu_dia = md.id AND mp.publicado = true
WHERE md.publicado = true
AND (
    (md.dia_semana = 'LUNES' AND dp.lunes = true) OR
    (md.dia_semana = 'MARTES' AND dp.martes = true) OR
    (md.dia_semana = 'MIERCOLES' AND dp.miercoles = true) OR
    (md.dia_semana = 'JUEVES' AND dp.jueves = true) OR
    (md.dia_semana = 'VIERNES' AND dp.viernes = true)
)
GROUP BY md.id, md.fecha, md.descripcion, md.dia_semana, md.publicado, 
         dp.lunes, dp.martes, dp.miercoles, dp.jueves, dp.viernes
ORDER BY md.dia_semana;

-- 6. Verificar si hay datos de ejemplo en general
SELECT 'usuarios' as tabla, COUNT(*) as total FROM usuarios
UNION ALL
SELECT 'dias_presenciales' as tabla, COUNT(*) as total FROM dias_presenciales
UNION ALL
SELECT 'menu_dia' as tabla, COUNT(*) as total FROM menu_dia
UNION ALL
SELECT 'menu_platos' as tabla, COUNT(*) as total FROM menu_platos
UNION ALL
SELECT 'platos' as tabla, COUNT(*) as total FROM platos;