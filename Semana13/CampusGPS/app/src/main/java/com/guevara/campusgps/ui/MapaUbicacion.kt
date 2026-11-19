package com.guevara.campusgps.ui

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.guevara.campusgps.model.UbicacionUi
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

// Solo dibuja lo que recibe: no consulta ningún Repository ni calcula distancias
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val ciclo = LocalLifecycleOwner.current

    // Un único MapView que se conserva entre recomposiciones
    val mapView = remember {
        configurarOsmdroid(contexto)
        MapView(contexto).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(-12.0464, -77.0428))
        }
    }

    // El MapView necesita enterarse del ciclo de vida de la pantalla
    DisposableEffect(ciclo, mapView) {
        val observador = LifecycleEventObserver { _, evento ->
            when (evento) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        ciclo.lifecycle.addObserver(observador)
        onDispose {
            ciclo.lifecycle.removeObserver(observador)
            mapView.onPause()
            mapView.onDetach()
        }
    }

    // Al llegar la ubicación, la cámara viaja hasta ella (zoom 16)
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            mapView.controller.animateTo(
                GeoPoint(estado.posicion.latitud, estado.posicion.longitud),
                16.0,
                800L
            )
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { mv -> dibujarMarcadores(mv, contexto, estado) }
        )
        // Atribución obligatoria de OpenStreetMap
        Text(
            text = "© OpenStreetMap contributors",
            color = Color.Black,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color(0xCCFFFFFF))
                .padding(horizontal = 4.dp)
        )
    }
}

private fun configurarOsmdroid(contexto: Context) {
    val cfg = Configuration.getInstance()
    cfg.load(contexto, contexto.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    // OpenStreetMap exige identificar la app en cada petición de mapas
    cfg.userAgentValue = contexto.packageName
    // Carpetas propias de la app: no hace falta permiso de almacenamiento
    cfg.osmdroidBasePath = File(contexto.filesDir, "osmdroid")
    cfg.osmdroidTileCache = File(contexto.cacheDir, "osmdroid_tiles")
}

// Marcador propio en rojo, solo si ya hay ubicación
private fun dibujarMarcadores(mv: MapView, contexto: Context, estado: UbicacionUi) {
    mv.overlays.clear()

    if (estado is UbicacionUi.Lista) {
        val yo = Marker(mv).apply {
            position = GeoPoint(estado.posicion.latitud, estado.posicion.longitud)
            title = "Estás aquí"
            icon = circulo(contexto, AndroidColor.rgb(229, 57, 53), 26)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        }
        mv.overlays.add(yo)
    }

    mv.invalidate()
}

private fun circulo(contexto: Context, color: Int, dp: Int): Drawable {
    val densidad = contexto.resources.displayMetrics.density
    val lado = (dp * densidad).toInt()
    return GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setColor(color)
        setStroke((2 * densidad).toInt(), AndroidColor.WHITE)
        setSize(lado, lado)
    }
}