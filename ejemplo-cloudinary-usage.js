/**
 * EJEMPLO DE USO DEL API DE CLOUDINARY PARA CARGAR PLATOS
 * 
 * El endpoint acepta multipart/form-data con dos partes:
 * 1. "plato": JSON con los datos del plato
 * 2. "imagen": Archivo de imagen que se sube a Cloudinary automáticamente
 */

// =====================================
// EJEMPLO 1: JAVASCRIPT/HTML
// =====================================

/*
<form id="platoForm" enctype="multipart/form-data">
    <input type="text" name="nombre" placeholder="Nombre del plato" required>
    <textarea name="descripcion" placeholder="Descripción" required maxlength="100"></textarea>
    <input type="text" name="categoria" placeholder="Categoría" required>
    
    <!-- Checkboxes para días de la semana -->
    <fieldset>
        <legend>Días disponibles:</legend>
        <label><input type="checkbox" name="dias" value="LUNES"> Lunes</label>
        <label><input type="checkbox" name="dias" value="MARTES"> Martes</label>
        <label><input type="checkbox" name="dias" value="MIERCOLES"> Miércoles</label>
        <label><input type="checkbox" name="dias" value="JUEVES"> Jueves</label>
        <label><input type="checkbox" name="dias" value="VIERNES"> Viernes</label>
    </fieldset>
    
    <input type="file" name="imagen" accept="image/*" required>
    <button type="submit">Cargar Plato</button>
</form>

<script>
document.getElementById('platoForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const formData = new FormData();
    
    // Obtener días seleccionados
    const diasSeleccionados = Array.from(document.querySelectorAll('input[name="dias"]:checked'))
                                   .map(checkbox => checkbox.value);
    
    if (diasSeleccionados.length === 0) {
        alert('Debe seleccionar al menos un día de la semana');
        return;
    }
    
    // Crear objeto con datos del plato
    const platoData = {
        nombre: document.querySelector('[name="nombre"]').value,
        descripcion: document.querySelector('[name="descripcion"]').value,
        categoria: document.querySelector('[name="categoria"]').value,
        diasSemana: diasSeleccionados
    };
    
    // Agregar datos del plato como JSON
    formData.append('plato', new Blob([JSON.stringify(platoData)], {
        type: 'application/json'
    }));
    
    // Agregar archivo de imagen
    const imageFile = document.querySelector('[name="imagen"]').files[0];
    formData.append('imagen', imageFile);
    
    try {
        const response = await fetch('/admin/cargarPlatos', {
            method: 'POST',
            body: formData,
            headers: {
                // Agregar token JWT si es necesario
                'Authorization': 'Bearer ' + localStorage.getItem('jwt-token')
            }
        });
        
        const result = await response.text();
        console.log('Resultado:', result);
        
        if (response.ok) {
            alert('¡Plato cargado exitosamente a Cloudinary!');
            document.getElementById('platoForm').reset();
        } else {
            alert('Error: ' + result);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error de conexión');
    }
});
</script>
*/

// =====================================
// EJEMPLO 2: ACTUALIZAR PLATO EXISTENTE
// =====================================

/*
// Para actualizar un plato existente (ID = 123):
const actualizarPlato = async (platoId) => {
    const formData = new FormData();
    
    const platoData = {
        nombre: "Pizza Actualizada",
        descripcion: "Nueva descripción del plato",
        categoria: "Pizza"
    };
    
    formData.append('plato', new Blob([JSON.stringify(platoData)], {
        type: 'application/json'
    }));
    
    // La imagen es opcional para actualización
    const imageFile = document.querySelector('[name="imagen"]').files[0];
    if (imageFile) {
        formData.append('imagen', imageFile);
    }
    
    const response = await fetch(`/admin/actualizarPlato/${platoId}`, {
        method: 'PUT',
        body: formData
    });
    
    const result = await response.text();
    console.log(result);
};
*/

// =====================================
// EJEMPLO 3: POSTMAN
// =====================================

/*
CREAR NUEVO PLATO:
1. Método: POST
2. URL: http://localhost:8080/admin/cargarPlatos
3. Body → form-data:
   - Key: "plato" | Type: "Text" | Value: {"nombre":"Pizza Margherita","descripcion":"Pizza con tomate y mozzarella","categoria":"Pizza","diasSemana":["LUNES","MIERCOLES","VIERNES"]}
   - Key: "imagen" | Type: "File" | Seleccionar archivo de imagen

ACTUALIZAR PLATO:
1. Método: PUT  
2. URL: http://localhost:8080/admin/actualizarPlato/123
3. Body → form-data:
   - Key: "plato" | Type: "Text" | Value: {"nombre":"Pizza Actualizada","descripcion":"Nueva descripción","categoria":"Pizza"}
   - Key: "imagen" | Type: "File" | (Opcional) Seleccionar nueva imagen

ELIMINAR PLATO:
1. Método: DELETE
2. URL: http://localhost:8080/admin/eliminarPlato/123

NOTA: En Postman, para el campo "plato", asegúrate de cambiar el Content-Type a "application/json"
*/

// =====================================
// EJEMPLO 4: CURL
// =====================================

/*
# Crear plato:
curl -X POST http://localhost:8080/admin/cargarPlatos \
  -F 'plato={"nombre":"Pizza Margherita","descripcion":"Pizza con tomate y mozzarella","categoria":"Pizza"};type=application/json' \
  -F 'imagen=@/ruta/a/tu/imagen.jpg;type=image/jpeg'

# Actualizar plato:
curl -X PUT http://localhost:8080/admin/actualizarPlato/123 \
  -F 'plato={"nombre":"Pizza Actualizada","descripcion":"Nueva descripción","categoria":"Pizza"};type=application/json' \
  -F 'imagen=@/ruta/a/nueva/imagen.jpg;type=image/jpeg'

# Eliminar plato:
curl -X DELETE http://localhost:8080/admin/eliminarPlato/123
*/

// =====================================
// CONFIGURACIÓN REQUERIDA
// =====================================

/*
ANTES DE USAR:

1. Configura Cloudinary:
   - Crea cuenta en https://cloudinary.com/
   - Copia .env.example a .env
   - Completa con tus credenciales de Cloudinary

2. Variables de entorno necesarias:
   - CLOUDINARY_CLOUD_NAME: tu cloud name
   - CLOUDINARY_API_KEY: tu API key
   - CLOUDINARY_API_SECRET: tu API secret

3. Las imágenes se suben automáticamente a Cloudinary en la carpeta "pedi2t/platos"
4. Las URLs generadas son públicas y optimizadas automáticamente
5. Al eliminar un plato, la imagen también se elimina de Cloudinary

ESTRUCTURA DE RESPUESTA:
- Éxito: "Plato cargado exitosamente. ID: 123, URL de imagen: https://res.cloudinary.com/..."
- Error: Mensaje de error específico
*/