package com.tecsup.pokemon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.admin.AdminScreen
import com.tecsup.pokemon.ui.admin.AdminViewModel
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.detalle.DetalleScreen
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosScreen
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.login.LoginScreen
import com.tecsup.pokemon.ui.login.LoginViewModel

object Rutas {
    const val LOGIN = "login"
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"
    const val ADMIN = "admin"
    fun detalle(id: Int) = "detalle/$id"
}

@Composable
fun AppNav(
    container: AppContainer,
    sesionViewModel: SesionViewModel,
    navController: NavHostController = rememberNavController()
) {
    val estadoSesion by sesionViewModel.estado.collectAsStateWithLifecycle()

    // Mientras se revisa si hay sesión guardada, se muestra Cargando
    val revisada = estadoSesion as? UiState.Exito<Sesion?> ?: run {
        Cargando(texto = "Abriendo la Pokédex...")
        return
    }
    // El destino inicial se calcula una sola vez
    val inicio = remember { if (revisada.datos == null) Rutas.LOGIN else Rutas.LISTA }

    // Nota: dentro del NavHost la sesión se lee del StateFlow en cada pantalla,
    // así el grafo de navegación no se reconstruye al iniciar o cerrar sesión.
    NavHost(navController = navController, startDestination = inicio) {

        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = viewModel(factory = LoginViewModel.factory(container)),
                onIngreso = { nueva ->
                    sesionViewModel.sesionIniciada(nueva)
                    navController.navigate(Rutas.LISTA) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.LISTA) {
            val sesion = sesionViewModel.sesionActual()
            ListaScreen(
                viewModel = viewModel(factory = ListaViewModel.factory(container)),
                esAdmin = sesion?.rol == Rol.ADMIN,
                correo = sesion?.correo ?: "",
                onAbrirDetalle = { id -> navController.navigate(Rutas.detalle(id)) },
                onFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                onAdmin = { navController.navigate(Rutas.ADMIN) },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            DetalleScreen(
                viewModel = viewModel(factory = DetalleViewModel.factory(container)),
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.FAVORITOS) {
            FavoritosScreen(
                viewModel = viewModel(factory = FavoritosViewModel.factory(container)),
                onAbrirDetalle = { id -> navController.navigate(Rutas.detalle(id)) },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.ADMIN) {
            val sesion = sesionViewModel.sesionActual()
            if (sesion?.rol == Rol.ADMIN) {
                AdminScreen(
                    viewModel = viewModel(factory = AdminViewModel.factory(container)),
                    onVolver = { navController.popBackStack() }
                )
            } else {
                // Un usuario normal no puede entrar al panel aunque conozca la ruta
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
    }
}

/** Lee la sesión actual desde el SesionViewModel (null si no hay sesión). */
@Composable
private fun SesionViewModel.sesionActual(): Sesion? {
    val valor by this.estado.collectAsStateWithLifecycle()
    return (valor as? UiState.Exito<Sesion?>)?.datos
}
