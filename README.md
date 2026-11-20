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
| 1 | Pantalla congelada o tarea con progreso | `![01-progreso.png](docs/capturas/01-progreso.png)` |
| 2 | Tarea cancelada | `![02-cancelada.png](docs/capturas/02-cancelada.png)` |
| 3 | Contar primos (resultado y tiempo) | `![03-primos.png](docs/capturas/03-primos.png)` |
| 4 | Una por una vs. a la vez | `![04-una-vs-a-la-vez.png](docs/capturas/04-una-vs-a-la-vez.png)` |
| 5 | Error simulado con Reintentar | `![05-error.png](docs/capturas/05-error.png)` |

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