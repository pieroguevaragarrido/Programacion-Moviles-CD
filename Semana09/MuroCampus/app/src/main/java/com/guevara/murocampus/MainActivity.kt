package com.guevara.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.guevara.murocampus.data.PostRepositoryRemote
import com.guevara.murocampus.data.remote.RetrofitClient
import com.guevara.murocampus.ui.PantallaLista
import com.guevara.murocampus.ui.PantallaDetalle
import com.guevara.murocampus.ui.PantallaCrear
import com.guevara.murocampus.ui.theme.MuroCampusTheme
import com.guevara.murocampus.viewmodel.PostsViewModel
import com.guevara.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                MuroCampusApp(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuroCampusApp(vm: PostsViewModel) {
    // Navegación sencilla: con idSeleccionado == null se ve la lista.
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }
    val enLista = idSeleccionado == null && !creando

    fun volver() {
        if (creando) creando = false else idSeleccionado = null
    }

    // El botón Atrás del sistema vuelve a la lista sin recargarla.
    BackHandler(enabled = !enLista) { volver() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MuroCampus", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (!enLista) {
                        IconButton(onClick = { volver() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (enLista) {
                FloatingActionButton(onClick = {
                    vm.limpiarPublicacion()
                    creando = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva publicación")
                }
            }
        }
    ) { padding ->
        val idActual = idSeleccionado
        when {
            creando -> PantallaCrear(vm, Modifier.padding(padding))
            idActual != null -> PantallaDetalle(vm, idActual, Modifier.padding(padding))
            else -> PantallaLista(
                vm = vm,
                onAbrirPost = { id ->
                    vm.abrir(id)
                    idSeleccionado = id
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}
