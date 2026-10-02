package com.guevara.tecsupstore.navigation

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guevara.tecsupstore.ui.screens.DetalleProductoScreen
import com.guevara.tecsupstore.ui.screens.FavoritosScreen
import com.guevara.tecsupstore.ui.screens.InicioScreen
import com.guevara.tecsupstore.ui.screens.PedidosScreen
import com.guevara.tecsupstore.ui.screens.PerfilScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("TECSUP Store") })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.INICIO) {
                InicioScreen(
                    onProductoClick = { id -> navController.navigate(Rutas.detalle(id)) }
                )
            }
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("productoId") ?: 0
                DetalleProductoScreen(
                    productoId = id,
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(Rutas.PEDIDOS) { PedidosScreen() }
            composable(Rutas.FAVORITOS) { FavoritosScreen() }
            composable(Rutas.PERFIL) { PerfilScreen() }
        }
    }
}
