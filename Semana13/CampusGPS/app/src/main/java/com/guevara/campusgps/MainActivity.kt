package com.guevara.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.guevara.campusgps.data.LugaresRepositoryLocal
import com.guevara.campusgps.data.UbicacionRepositoryGoogle
import com.guevara.campusgps.ui.PantallaMapa
import com.guevara.campusgps.ui.theme.CampusGPSTheme
import com.guevara.campusgps.viewmodel.CampusViewModelFactory
import com.guevara.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    private val vm: MapaViewModel by viewModels {
        CampusViewModelFactory(
            UbicacionRepositoryGoogle(applicationContext),
            LugaresRepositoryLocal()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusGPSTheme {
                PantallaMapa(vm)
            }
        }
    }
}
