package com.tecsup.pokemon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.pokemon.ui.navigation.AppNav
import com.tecsup.pokemon.ui.navigation.SesionViewModel
import com.tecsup.pokemon.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as PokemonApp).container

        setContent {
            PokedexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PedirPermisoNotificaciones()
                    val sesionViewModel: SesionViewModel =
                        viewModel(factory = SesionViewModel.factory(container))
                    AppNav(container = container, sesionViewModel = sesionViewModel)
                }
            }
        }
    }
}

/** En Android 13+ se pide POST_NOTIFICATIONS. Si se niega, la app sigue igual. */
@Composable
private fun PedirPermisoNotificaciones() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* concedido o no, no hay nada más que hacer */ }

    LaunchedEffect(Unit) {
        val concedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!concedido) launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
