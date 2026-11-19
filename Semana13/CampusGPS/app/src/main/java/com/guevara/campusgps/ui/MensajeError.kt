package com.guevara.campusgps.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guevara.campusgps.model.TipoError
import com.guevara.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = error.mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            when (error.tipo) {
                TipoError.SIN_PERMISO ->
                    Button(onClick = onAbrirAjustes) { Text("Abrir ajustes") }

                TipoError.GPS_APAGADO, TipoError.OTRO ->
                    Button(onClick = onReintentar) { Text("Reintentar") }

                TipoError.SIN_PLAY_SERVICES -> Unit // sin botón de reintentar
            }
        }
    }
}
