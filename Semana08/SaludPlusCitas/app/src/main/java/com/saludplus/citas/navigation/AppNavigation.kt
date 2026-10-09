package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.screens.agendamiento.*
import com.saludplus.citas.ui.screens.auth.*
import com.saludplus.citas.ui.screens.citas.*
import com.saludplus.citas.ui.screens.home.*
import com.saludplus.citas.ui.screens.notificaciones.*
import com.saludplus.citas.ui.screens.perfil.*
import com.saludplus.citas.ui.screens.resultados.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Splash.ruta) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(onNavegarRegistro = { navController.navigate(Rutas.Registro.ruta) })
        }
        composable(Rutas.Registro.ruta) {
            RegistroScreen(
                onNavegarHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onNavegarLogin = { navController.navigate(Rutas.Login.ruta) },
                onNavegarTerminos = { navController.navigate(Rutas.Terminos.ruta) }
            )
        }
        composable(Rutas.Login.ruta) {
            LoginScreen(
                onNavegarHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onNavegarRegistro = { navController.navigate(Rutas.Registro.ruta) }
            )
        }
        composable(Rutas.Home.ruta) {
            HomeScreen(
                onNavegarEspecialidades = { navController.navigate(Rutas.Especialidades.ruta) },
                onNavegarMisCitas = { navController.navigate(Rutas.MisCitas.ruta) },
                onNavegarPerfil = { navController.navigate(Rutas.Perfil.ruta) },
                onNavegarResultados = { navController.navigate(Rutas.Resultados.ruta) },
                onNavegarNotificaciones = { navController.navigate(Rutas.Notificaciones.ruta) }
            )
        }
        composable(Rutas.Especialidades.ruta) {
            EspecialidadesScreen(
                onSeleccionarEspecialidad = { espId ->
                    navController.navigate(Rutas.Medicos.crearRuta(espId))
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.Medicos.ruta,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { backStackEntry ->
            val espId = backStackEntry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(
                especialidadId = espId,
                onSeleccionarMedico = { medId ->
                    navController.navigate(Rutas.FechaHora.crearRuta(medId))
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.FechaHora.ruta,
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            FechaHoraScreen(
                medicoId = medId,
                onContinuar = { fecha, hora ->
                    navController.navigate(Rutas.ConfirmarCita.crearRuta(medId, fecha, hora))
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.ConfirmarCita.ruta,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            ConfirmarCitaScreen(
                medicoId = medId,
                fecha = fecha,
                hora = hora,
                onCitaConfirmada = { citaId ->
                    navController.navigate(Rutas.CitaExitosa.crearRuta(citaId)) {
                        popUpTo(Rutas.Home.ruta) { inclusive = false }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.CitaExitosa.ruta,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            CitaExitosaScreen(
                citaId = citaId,
                onIrAMisCitas = {
                    navController.navigate(Rutas.MisCitas.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = false }
                    }
                },
                onIrAInicio = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                }
            )
        }
        composable(Rutas.MisCitas.ruta) {
            MisCitasScreen(
                onVerDetalle = { citaId ->
                    navController.navigate(Rutas.DetalleCita.crearRuta(citaId))
                },
                onNavegarHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                },
                onNavegarPerfil = { navController.navigate(Rutas.Perfil.ruta) },
                onNavegarResultados = { navController.navigate(Rutas.Resultados.ruta) }
            )
        }
        composable(
            route = Rutas.DetalleCita.ruta,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            DetalleCitaScreen(
                citaId = citaId,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.Perfil.ruta) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.Splash.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavegarHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                },
                onNavegarMisCitas = { navController.navigate(Rutas.MisCitas.ruta) },
                onNavegarResultados = { navController.navigate(Rutas.Resultados.ruta) }
            )
        }
        composable(Rutas.Resultados.ruta) {
            ResultadosScreen(
                onVolver = { navController.popBackStack() },
                onNavegarHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                },
                onNavegarMisCitas = { navController.navigate(Rutas.MisCitas.ruta) },
                onNavegarPerfil = { navController.navigate(Rutas.Perfil.ruta) }
            )
        }
        composable(Rutas.Notificaciones.ruta) {
            NotificacionesScreen(onVolver = { navController.popBackStack() })
        }
        composable(Rutas.Terminos.ruta) {
            TerminosScreen(onVolver = { navController.popBackStack() })
        }
    }
}
