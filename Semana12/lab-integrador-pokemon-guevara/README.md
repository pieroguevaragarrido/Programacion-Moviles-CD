# Pokédex Fuego — Laboratorio 11-12 (Programación en Móviles)

**Compose + MVVM + Retrofit + Room + Firebase Auth**
Docente: Juan José León S. · TECSUP

| Dato | Valor |
|---|---|
| Integrante | Piero Guevara (trabajo individual) |
| Tema | Pokémon — API `https://pokeapi.co/api/v2/` |
| Variante | Tipo **fire** (`/type/fire`) |
| Endpoint lista | `GET /type/{tipo}` |
| Endpoint detalle | `GET /pokemon/{nombre}` |
| Paquete | `com.tecsup.pokemon` |

> Para cambiar de variante (water o grass) se cambia solo `ConfigApi.TIPO` en `data/remote/RetrofitClient.kt`.

---

## 1. Qué hace la app

- **Login** con correo y contraseña (Firebase Authentication). Valida campos vacíos sin llamar a Firebase y muestra el error si las credenciales fallan.
- **Dos roles** leídos de Firestore (`usuarios/{uid}.rol`): `admin` y `usuario`.
- **Catálogo** de Pokémon tipo fuego: lista en cuadrícula con estados *Cargando*, *Error (Reintentar)* y *Vacío*.
- **Detalle** con el segundo endpoint: tipos, altura, peso, experiencia base, habilidades y estadísticas.
- **Favoritos** guardados en Room; sobreviven al cierre de la app y a la sincronización.
- **Modo sin conexión**: se muestra lo guardado con un aviso. Si no hay nada guardado, error con Reintentar.
- **Notificación local** «Se guardaron N elementos» al terminar de sincronizar (canal + permiso `POST_NOTIFICATIONS`). Al tocarla se abre la app.
- **Panel Admin** (solo admin): total guardado, total de favoritos, Sincronizar y Limpiar caché.
- **Sesión** persistente al reabrir la app y botón Cerrar sesión.

## 2. Arquitectura (MVVM)

```
UI (Compose) ──eventos──▶ ViewModel ──▶ Repository (interface)
     ▲                       │                ├─ Retrofit (DTO)   → solo actualiza Room
     └──── StateFlow<UiState>│                ├─ Room (Entity)    → fuente única de verdad
                             ▼                └─ Firebase (Auth + Firestore: rol)
                     Notificador (interface)
```

```
app/src/main/java/com/tecsup/pokemon/
├── MainActivity.kt            ← pide permiso de notificaciones y arranca AppNav
├── PokemonApp.kt              ← Application: crea el AppContainer
├── AppContainer.kt            ← crea API, base de datos y repositorios
├── data/
│   ├── remote/  ApiService.kt · RetrofitClient.kt · dto/ItemDto.kt
│   ├── local/   ItemEntity.kt · ItemDao.kt · AppDatabase.kt
│   ├── auth/    AuthRepository.kt (interface) · FirebaseAuthRepository.kt
│   ├── repository/ ItemRepository.kt (interface) · ItemRepositoryImpl.kt
│   └── mapper/Mappers.kt      ← ItemDto → ItemEntity → Item
├── domain/model/ Item.kt · Sesion.kt (+ enum Rol)
├── notification/ Notificador.kt (interface) · NotificadorAndroid.kt
└── ui/
    ├── UiState.kt
    ├── navigation/ AppNav.kt · SesionViewModel.kt
    ├── login/      LoginScreen.kt · LoginViewModel.kt
    ├── lista/      ListaScreen.kt · ListaViewModel.kt
    ├── detalle/    DetalleScreen.kt · DetalleViewModel.kt
    ├── favoritos/  FavoritosScreen.kt · FavoritosViewModel.kt
    ├── admin/      AdminScreen.kt · AdminViewModel.kt
    ├── components/ Cargando.kt · ErrorView.kt · PokemonCard.kt
    └── theme/
```

Reglas que se cumplen:
- Las pantallas solo observan con `collectAsStateWithLifecycle()` y llaman funciones del ViewModel. Ningún composable usa Retrofit, Room ni Firebase.
- Los ViewModel no reciben `Context`: la notificación se pide con la interface `Notificador`.
- Los ViewModel dependen de las interfaces `ItemRepository` y `AuthRepository`, y se crean con un `ViewModelProvider.Factory` que recibe el `AppContainer`.
- Tres modelos: `ItemDto` (API), `ItemEntity` (Room) e `Item` (UI), convertidos en `Mappers.kt`.
- La lista sale siempre de Room. `refrescar()` lee los ids favoritos **antes** del upsert y los vuelve a aplicar, así no se pierden.
- El rol se decide en `FirebaseAuthRepository.leerSesion()` (capa de datos), no en la pantalla.

##. Ramas

- `main`: Fase 1 (sin IA).
- `mejora-ia`: Fase 2 (con IA), con `PROMPTS.md` y Pull Request hacia `main`.

Trabajo individual: Piero Guevara desarrolló todas las capas (datos, dominio, UI, Firebase y notificaciones).
