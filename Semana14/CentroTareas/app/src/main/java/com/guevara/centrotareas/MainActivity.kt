package com.guevara.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.guevara.centrotareas.ui.PantallaTareas
import com.guevara.centrotareas.ui.theme.CentroTareasTheme
import com.guevara.centrotareas.viewmodel.TareasViewModel
import com.guevara.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    // El ViewModel sobrevive a la rotación de pantalla
    private val viewModel: TareasViewModel by viewModels { TareasViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                PantallaTareas(vm = viewModel)
            }
        }
    }
}
