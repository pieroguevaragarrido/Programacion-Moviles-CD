Prompts utilizados - Fase 2 (mejora-ia)

Prompt 1: Cancelar cita con confirmación

Prompt usado:

Quiero agregar una mejora funcional a mi app ClinicaSalud (Jetpack Compose).
NO cambies la logica de navegacion existente (NavHost, Screen.kt, argumentos
tipados con NavType). Solo agrega la siguiente funcionalidad:

CONTEXTO
- MyAppointmentsScreen.kt muestra una lista de citas (CitaGuardada: medico,
  fecha, hora, confirmada) usando LazyColumn y Card, con datos de prueba
  en citasDePrueba.

MEJORA A AGREGAR: Cancelar cita con confirmacion

1. Cada Card de una cita con estado "Confirmada" debe mostrar un boton o
   icono de "Cancelar cita".
2. Al presionarlo, se debe abrir un AlertDialog preguntando
   "¿Estas seguro de cancelar esta cita con [nombre del medico]?"
   con boton "Si, cancelar" y boton "No".
3. Si el usuario confirma, la cita debe eliminarse de la lista visible
   (usa remember { mutableStateListOf(...) } en vez de una lista fija,
   para que la UI se actualice al eliminar un elemento).
4. Si el usuario cancela el dialogo, no debe pasar nada y el dialogo
   se cierra.
5. Las citas con estado "Completada" NO deben mostrar el boton de cancelar.

RESTRICCIONES
- Mantén el nombre de la funcion MyAppointmentsScreen y su parametro
  navController
- No toques Screen.kt, AppNavigation.kt ni las demas pantallas
- Usa componentes de Material3 (AlertDialog, TextButton, IconButton)
- El codigo debe compilar sin errores nuevos