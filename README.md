# CentroTareas

App Android hecha con **Kotlin + Jetpack Compose + Material 3** (sin XML), corrutinas y ViewModel.
Curso: Programación en Móviles · Semana 14 · *Hilos en Android*.

**Autor:** Guevara · **Paquete:** `com.guevara.centrotareas`

## Descripción

CentroTareas realiza tres tareas que tardan y demuestra que la pantalla **nunca se congela**
cuando el trabajo se hace en segundo plano con corrutinas:

1. **Tarea con progreso:** barra de 0 a 100 % (≈ 4 s) que se puede cancelar.
2. **Contar números primos:** cálculo pesado en `Dispatchers.Default`, cancelable.
3. **Dos cargas:** perfil (2 s) y pedidos (3 s) cargados *una por una* (≈ 5 s) o *a la vez* con `async` (≈ 3 s), con estado de error y botón **Reintentar**.

El botón **Tócame** es la prueba de vida: si deja de sumar, el hilo principal está bloqueado.
Todos los datos son ficticios y no se usa internet ni permisos.

## Árbol de paquetes

```
com.guevara.centrotareas/
├── MainActivity.kt
├── model/
│   └── Estados.kt                  PrimosUi, CargaUi
├── data/
│   ├── TareasRepository.kt         interface
│   └── TareasRepositoryLocal.kt    cargas simuladas y contarPrimos
├── viewmodel/
│   ├── TareasViewModel.kt
│   └── TareasViewModelFactory.kt
└── ui/
    ├── PantallaTareas.kt           las tres tarjetas
    └── theme/                      Color.kt, Theme.kt
```

## Arquitectura MVVM + Repository

```
View (PantallaTareas)
   | eventos: botones                ^ collectAsState()
   v                                 |
ViewModel (TareasViewModel) --- StateFlow --->
   | viewModelScope.launch { ... }
   v
TareasRepository (interface) ---> TareasRepositoryLocal
                                  (cargas simuladas y contar primos)
```

| Capa | Conoce a | No debe |
| --- | --- | --- |
| View (`ui/`) | Solo a su ViewModel | Lanzar corrutinas ni usar hilos; solo muestra el estado y avisa los eventos |
| ViewModel | Solo a la interface del Repository | Conocer Composables, recibir un Context ni bloquear el hilo principal |
| Repository | Nada de la UI | Exponer estados de pantalla; sus `suspend` no bloquean el hilo que las llama |

## Observación del Paso 1

Al tocar **Tarea larga (MAL)** y enseguida **Tócame**, la pantalla queda **completamente congelada
durante 4 segundos**: el botón no responde y el contador no suma. Cuando pasan los 4 segundos, de golpe
aparece **Terminó**.

Nunca se llega a ver **Trabajando...** porque `Thread.sleep(4000)` se ejecuta en el **hilo principal**,
el mismo que dibuja la pantalla. El mensaje sí se escribe en el estado, pero Compose no puede
redibujar hasta que la función termine; cuando el hilo se libera, el estado ya dice **Terminó**
y esa es la única versión que se pinta.

Si la espera fuera de **10 segundos**, Android detectaría que la app no atiende los eventos de entrada
(a partir de ~5 s) y mostraría el diálogo **ANR** («Application Not Responding» / «La app no responde»)
ofreciendo *Cerrar app* o *Esperar*.

## Capturas

Las capturas están en la carpeta [`docs/capturas/`](docs/capturas/):

| # | Captura | Archivo |
| --- | --- | --- |
| 1 | Pantalla congelada o tarea con progreso | `docs/capturas/01-progreso.png` |
| 2 | Tarea cancelada | `docs/capturas/02-cancelada.png` |
| 3 | Contar primos (resultado y tiempo) | `docs/capturas/03-primos.png` |
| 4 | Una por una vs. a la vez | `docs/capturas/04-una-vs-a-la-vez.png` |
| 5 | Error simulado con Reintentar | `docs/capturas/05-error.png` |

## Pruebas de aceptación (Paso 8)

| Prueba | Resultado esperado | OK |
| --- | --- | --- |
| Tarea larga: tocar **Tócame** mientras trabaja | El contador sigue sumando | ☐ |
| Logcat con filtro `HILO` | `main` antes y después; `DefaultDispatcher-worker` dentro | ☐ |
| Iniciar la tarea con progreso | La barra avanza de 0 a 100 % en ≈ 4 s | ☐ |
| Cancelar a mitad | La barra se detiene, dice **Cancelado** y se puede iniciar otra vez | ☐ |
| Pulsar **Iniciar** dos veces seguidas | No se lanzan dos tareas (el botón está deshabilitado) | ☐ |
| Primos hasta 1000 | Resultado **168** | ☐ |
| Primos con N grande | La pantalla no se congela y **Cancelar** funciona | ☐ |
| Una por una | ≈ 5 s y resultado correcto | ☐ |
| A la vez | ≈ 3 s y resultado correcto | ☐ |
| Error simulado + **Reintentar** | Recuadro rojo; al desactivar el interruptor y reintentar, éxito | ☐ |
| Rotar la pantalla durante la tarea | La tarea continúa y el progreso no se reinicia | ☐ |
| Revisar el código | `git grep -nE "GlobalScope\|runBlocking\|Thread\.sleep" -- '*.kt'` no encuentra nada | ☐ |

## Para pensar

**1. ¿Por qué no se veía «Trabajando...» en el Paso 1?**
Porque `Thread.sleep` bloqueaba el hilo principal, que es el que dibuja. El texto cambió en el estado,
pero la pantalla no pudo redibujarse hasta que la función terminó; para entonces el mensaje ya era «Terminó».

**2. ¿Qué diferencia hay entre `Thread.sleep` y `delay`?**
`Thread.sleep` **bloquea el hilo** completo: nadie más puede usarlo mientras espera. `delay` **suspende
solo la corrutina**: libera el hilo para otras tareas y lo retoma cuando termina el tiempo. Además,
`delay` es cancelable: revisa la cancelación y lanza `CancellationException`.

**3. ¿Por qué `Dispatchers.Default` para los primos y `Dispatchers.IO` para la red?**
Los primos son trabajo de **CPU**: conviene un pool con tantos hilos como núcleos (`Default`), porque
más hilos no lo harían más rápido. La red y el disco **esperan** casi todo el tiempo, así que `IO` usa
un pool mucho más grande para que muchas esperas puedan convivir sin quedarse sin hilos.

**4. ¿Qué pasaría con la corrutina si el usuario cierra la pantalla? ¿Qué papel cumple `viewModelScope`?**
`viewModelScope` está ligado al ciclo de vida del ViewModel: cuando el usuario sale de la pantalla
definitivamente y el ViewModel se destruye (`onCleared`), el scope se **cancela** y con él todas sus
corrutinas, evitando fugas de memoria y trabajo inútil. En una **rotación** el ViewModel sobrevive,
por eso la tarea continúa y el progreso no se reinicia.

**5. ¿Por qué `async` tarda 3 s y no 5 s? ¿Qué pasa si una de las dos cargas falla?**
Porque las dos llamadas arrancan **a la vez**: el tiempo total es el de la más lenta,
`máximo(2 s, 3 s) ≈ 3 s`, y no la suma `2 s + 3 s`. Dentro de `coroutineScope`, si una falla se **cancela
la otra** y la excepción sube al `try/catch`, que la convierte en el estado `Error` sin cerrar la app.
