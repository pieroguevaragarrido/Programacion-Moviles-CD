package com.guevara.tecsupstore.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route ?: Rutas.INICIO

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "TECSUP Store",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = rutaActual == Rutas.INICIO,
                    onClick = {
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Mis Pedidos") },
                    label = { Text("Mis Pedidos") },
                    selected = rutaActual == Rutas.PEDIDOS,
                    onClick = {
                        navController.navigate(Rutas.PEDIDOS)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") },
                    selected = rutaActual == Rutas.FAVORITOS,
                    onClick = {
                        navController.navigate(Rutas.FAVORITOS)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = rutaActual == Rutas.PERFIL,
                    onClick = {
                        navController.navigate(Rutas.PERFIL)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                var menuExpandido by remember { mutableStateOf(false) }

                TopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { menuExpandido = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                        }
                        DropdownMenu(
                            expanded = menuExpandido,
                            onDismissRequest = { menuExpandido = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Configuración") },
                                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                onClick = { menuExpandido = false }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                onClick = { menuExpandido = false }
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Rutas.INICIO,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Rutas.INICIO) {
                    InicioScreen(
                        onProductoClick = { productoId ->
                            navController.navigate("${Rutas.DETALLE}/$productoId")
                        }
                    )
                }
                composable(
                    route = "${Rutas.DETALLE}/{productoId}",
                    arguments = listOf(navArgument("productoId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("productoId") ?: 0
                    DetalleProductoScreen(
                        productoId = id,
                        onVolver = { navController.popBackStack() }
                    )
                }
                composable(Rutas.PEDIDOS) {
                    PedidosScreen()
                }
                composable(Rutas.FAVORITOS) {
                    FavoritosScreen()
                }
                composable(Rutas.PERFIL) {
                    PerfilScreen()
                }
            }
        }
    }
}