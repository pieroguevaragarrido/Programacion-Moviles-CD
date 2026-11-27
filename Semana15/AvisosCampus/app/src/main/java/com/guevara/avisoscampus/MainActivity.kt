package com.guevara.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.guevara.avisoscampus.data.AvisosRepositoryMemoria
import com.guevara.avisoscampus.ui.PantallaAvisos
import com.guevara.avisoscampus.ui.theme.AvisosCampusTheme
import com.guevara.avisoscampus.viewmodel.AvisosViewModel
import com.guevara.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: AvisosViewModel by viewModels {
        AvisosViewModelFactory(AvisosRepositoryMemoria())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                PantallaAvisos(vm)
            }
        }
    }
}
