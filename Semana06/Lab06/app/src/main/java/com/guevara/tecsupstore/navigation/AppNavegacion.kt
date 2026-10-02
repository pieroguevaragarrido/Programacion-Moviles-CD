package com.guevara.tecsupstore.navigation

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guevara.tecsupstore.ui.screens.DetalleProductoScreen
import com.guevara.tecsupstore.ui.screens.FavoritosScreen
import com.guevara.tecsupstore.ui.screens.InicioScreen
import com.guevara.tecsupstore.ui.screens.PedidosScreen
import com.guevara.tecsupstore.ui.screens.PerfilScreen
import kotlinx.coroutines.launch

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
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Inicio", modifier = Modifier.padding(16.dp))
                    Text("Mis pedidos", modifier = Modifier.padding(16.dp))
                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = currentRoute == Rutas.INICIO,
                        onClick = {
                            navController.navigate(Rutas.INICIO)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Mis pedidos") },
                        selected = currentRoute == Rutas.PEDIDOS,
                        onClick = {
                            navController.navigate(Rutas.PEDIDOS)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Favoritos") },
                        selected = currentRoute == Rutas.FAVORITOS,
                        onClick = {
                            navController.navigate(Rutas.FAVORITOS)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Perfil") },
                        selected = currentRoute == Rutas.PERFIL,
                        onClick = {
                            navController.navigate(Rutas.PERFIL)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    val currentRoute = null
                    NavigationDrawerItem(
                        label = { Text("Cerrar sesion") },
                        selected = currentRoute == "login",
                        onClick = {
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        ) {
            // Aquí va tu Scaffold o NavHost actual.
        }
    }
}
