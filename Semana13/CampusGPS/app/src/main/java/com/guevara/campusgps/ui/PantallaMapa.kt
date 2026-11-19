package com.guevara.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.guevara.campusgps.model.UbicacionUi
import com.guevara.campusgps.viewmodel.MapaViewModel
import java.util.Locale
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import com.guevara.campusgps.util.formatearDistancia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val contexto = LocalContext.current

    // Solicitud del permiso en tiempo de ejecución
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        // Si el usuario aceptó alguno de los dos permisos, buscamos la ubicación
        if (resultado.values.any { it }) vm.buscar()
    }

    fun tienePermiso(): Boolean =
        ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    contexto, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    fun alPulsar() {
        if (tienePermiso()) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { alPulsar() },
                        enabled = estado !is UbicacionUi.Buscando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Obtener mi ubicación")
                    }
                    OutlinedButton(
                        onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                        enabled = masCercano != null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Ir al más cercano")
                    }
                }

                when (val e = estado) {
                    is UbicacionUi.Buscando -> Text("Buscando tu ubicación…")
                    is UbicacionUi.Lista -> Text(
                        String.format(
                            Locale.US, "Lat: %.5f  ·  Lon: %.5f",
                            e.posicion.latitud, e.posicion.longitud
                        )
                    )
                    is UbicacionUi.Error -> Text(e.mensaje)
                    UbicacionUi.Inicial -> Unit
                }
                // Texto: Más cercano: nombre (distancia)
                val cercano = masCercano
                val metrosCercano = lugares
                    .firstOrNull { it.lugar.id == cercano?.id }?.metros
                if (cercano != null && metrosCercano != null) {
                    Text(
                        text = "Más cercano: ${cercano.nombre} (${formatearDistancia(metrosCercano)})",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = { vm.seleccionar(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }
    }
}