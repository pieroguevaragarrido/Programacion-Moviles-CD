# PROMPTS.md · Fase 2 (mejora con IA)

Herramienta: **Gemini en Android Studio**. Rama de trabajo: `mejora-ia-s14`.
Cada prompt pedía una sola mejora e incluía las reglas del laboratorio: sin `GlobalScope`,
`runBlocking` ni `Thread.sleep`, relanzar siempre `CancellationException` y sin dependencias nuevas.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| «Mueve la lógica de la tarea con progreso del ViewModel al Repository: agrega `progresoTarea(): Flow<Float>` con `flow { }` y `flowOn(Dispatchers.Default)`, y haz que `tareaLarga()` lo recoja con `collect`. Mantén `Job`, `corriendo`, `cancelar()` y los mensajes.» | Agregó `progresoTarea()` a la interface y a `TareasRepositoryLocal` con `flow` + `flowOn(Default)`. En el ViewModel reemplazó el bucle por `repo.progresoTarea().collect`. Funcionaba, pero **borró los `Log.d("HILO", ...)`** que ya existían en `tareaLarga()`. | **Restauré los logs**: `Antes` y `Despues` en el ViewModel y `Dentro` dentro del `flow` del Repository. Quité comentarios largos y dejé nombres propios (`paso` en vez de `i`). Mantuve el `finally` y el bloqueo de doble inicio. | La prueba 2 del Paso 8 pide ver `main` → `DefaultDispatcher-worker` → `main` en Logcat; sin los logs no se puede demostrar que el trabajo corre fuera del hilo principal. Poner el log dentro del `flow` prueba además que `flowOn` funciona. |
| «Usa `withTimeout` en `cargarUnaPorUna()` y `cargarALaVez()`. Si se excede, estado `CargaUi.Error("Tardó demasiado")` con el botón Reintentar. Ojo: `TimeoutCancellationException` es una `CancellationException`; captúrala antes del `catch` general.» | Envolvió ambas cargas con `withTimeout(4000)` y puso `catch (e: TimeoutCancellationException)` **antes** del `catch (e: Exception)` (esto estaba bien). Pero copió el mismo `try/catch` completo en las dos funciones y fijó el límite en **4 s**. | (1) Unifiqué la lógica en una función privada `lanzarCarga(modo, tarea)`; las dos cargas solo definen qué llamar. (2) **Cambié el límite**: por defecto 6 s y ajustable de 1 a 8 s con un `Slider` en la tarjeta (`limiteSegundos` en el ViewModel). (3) Dejé un comentario explicando por qué el `catch` del timeout va primero. | Con 4 s, **Una por una** (≈ 5 s) fallaba siempre, así que dejaba de cumplir la prueba «Una por una ≈ 5 s» y nunca llegaba a la tabla comparativa. Con 6 s por defecto todo funciona y, bajando el deslizador, se puede mostrar el error. Código duplicado = dos lugares donde corregir el mismo error. |
| «Agrega una tabla que acumule los tiempos de Una por una y A la vez: `RegistroTiempo(modo, milisegundos)`, un `StateFlow<List<RegistroTiempo>>` en el ViewModel (máx. 10) y la tabla en la tarjeta Dos cargas, con el promedio de cada modo. Sin librerías nuevas.» | Creó `RegistroTiempo`, el `historial` (máx. 10) y una tabla con `Row`/`Column`. **Calculó los promedios dentro del Composable** (`filter`, `map`, `average` en la View) y repitió los textos `"Una por una"` y `"A la vez"` en varios archivos. | Moví el cálculo a una función de extensión `promedioDe(modo)` en el paquete `model`. Creé el objeto `Modo` con las constantes de los dos modos. Separé la tabla en los composables `TablaTiempos` y `FilaTabla` y usé `HorizontalDivider`. El registro solo se guarda cuando la carga termina con éxito. | La View solo debe mostrar estado; calcular promedios en cada recomposición mezcla lógica con UI. Las constantes evitan errores de tipeo entre archivos (si un texto cambia, el promedio saldría vacío sin avisar). |

## Revisión del código generado

Lo que se comprobó antes de aceptar el código de Gemini:

- No aparecen `GlobalScope`, `runBlocking` ni `Thread.sleep` en ningún `.kt` (`git grep -nE "GlobalScope|runBlocking|Thread\.sleep" -- "*.kt"`).
- No se agregaron dependencias ni se modificaron `build.gradle.kts` ni `AndroidManifest.xml`.
- La View solo lee `StateFlow` con `collectAsState` y no lanza corrutinas; el ViewModel no recibe `Context`.
- Toda `CancellationException` se relanza; la del timeout se atrapa aparte, porque es un caso esperado y no una cancelación del usuario.

Lo que Gemini hizo mal o dejó a medias y se corrigió está en la columna «Qué cambié o descarté»: logs borrados, límite de 4 s que rompía la comparación, `try/catch` duplicado y lógica de promedios en la View.
