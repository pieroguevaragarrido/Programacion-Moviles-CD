# PROMPTS.md · CampusGPS · Fase 2

Usé Gemini en Android Studio con un mensaje inicial de contexto (arquitectura MVVM + Repository, mapa con osmdroid, sin Google Maps ni claves, View solo habla con su ViewModel, ViewModel sin Context, cálculos fuera de los Composables) y después un prompt por mejora.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| «Mejora 1: filtrar por categoría con chips (Estudio, Comida, Eventos, Servicios y "Todos"). El estado va en el ViewModel, la lista se deriva con combine, y si el lugar seleccionado queda oculto se deselecciona. La View solo dibuja los chips» | Agregó `_categoria` al ViewModel, derivó `lugares` con `combine` y creó chips `FilterChip` con la opción "Todos" | Moví la lista de categorías, que estaba escrita a mano en la View, al ViewModel como `categorias`, tomada del repositorio. Simplifiqué `cambiarCategoria` a una sola condición | La View solo debe dibujar. Si cambian los lugares, las categorías salen solas, sin tocar la pantalla |
| «Mejora 2: seguimiento en vivo. Agrega `ubicacionEnVivo(): Flow<Posicion>` al Repository con `callbackFlow` y `requestLocationUpdates`, `awaitClose` para quitar las actualizaciones, un estado `siguiendo` y botón Iniciar/Detener. El ViewModel no usa Context» | Agregó el método a la interfaz y a la implementación con `callbackFlow`, un `Job` y el estado `siguiendo` en el ViewModel, y un botón que alterna entre iniciar y detener | Uní los dos `catch` iguales del repositorio. Extraje `hayPlayServices()` porque `buscar()` e `iniciarSeguimiento()` repetían el mismo bloque. En la View limpio `accionPendiente` después de usarla | Evitar código duplicado y que una acción vieja quede guardada. Comprobé que el ViewModel no usa Context y que `awaitClose` libera las actualizaciones |
| «Mejora 3: selector de radio de 100, 200 o 500 m. El cálculo de qué lugares están dentro va en el ViewModel. Dibuja el círculo con osmdroid (no con Circle de Google Maps) y marca en la lista los lugares dentro del radio» | Agregó `radio` y `enRadioIds` (con `combine`) al ViewModel, un `Polygon.pointsAsCircle` en `MapaUbicacion` y el texto "Dentro del radio" en la lista | Moví las opciones de radio de la View al ViewModel (`opcionesRadio`) y reduje la altura de la lista de 220 a 180 dp | El cálculo de lugares dentro del radio ya estaba en el ViewModel, no en un Composable. Con más controles, el mapa quedaba muy chico |

## Reglas de MVVM que verifiqué
- El ViewModel no usa `Context`, solo las interfaces de los repositorios.
- Los Composables no calculan distancias, filtros ni radios; solo dibujan lo que reciben.
- La clave de API no existe en el repositorio (OpenStreetMap no la necesita).

