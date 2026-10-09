package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.TopBarSaludPlus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (fecha: String, hora: String) -> Unit,
    onVolver: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    // Estado para el desplazamiento por semanas (0 = semana actual)
    var offsetSemanas by remember { mutableIntStateOf(0) }

    // Calcula los 5 días hábiles según el desplazamiento de semana
    val diasHabiles = remember(offsetSemanas) {
        obtenerProximosDiasHabiles(offsetSemanas)
    }

    var fechaSeleccionada by remember(offsetSemanas) { mutableStateOf(diasHabiles.first()) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val fmtIso = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fmtDiaBoton = DateTimeFormatter.ofPattern("EEE dd", Locale("es", "ES"))
    val fmtMesAño = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))

    val fechaIso = fechaSeleccionada.format(fmtIso)
    val horariosDisponibles = Repositorio.horariosDisponibles(medicoId, fechaIso)

    val tituloMesAño = fechaSeleccionada.format(fmtMesAño)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Seleccionar fecha y hora", onVolver = onVolver) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Dr(a). ${medico?.nombre ?: ""}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Selector de semana con mes y año dinámico
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (offsetSemanas > 0) {
                            offsetSemanas--
                            horaSeleccionada = null
                        }
                    },
                    enabled = offsetSemanas > 0
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Semana anterior")
                }

                Text(
                    text = tituloMesAño,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                    onClick = {
                        offsetSemanas++
                        horaSeleccionada = null
                    }
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Semana siguiente")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Días hábiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                diasHabiles.forEach { dia ->
                    val textoBoton = dia.format(fmtDiaBoton)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                    FilterChip(
                        selected = fechaSeleccionada == dia,
                        onClick = {
                            fechaSeleccionada = dia
                            horaSeleccionada = null
                        },
                        label = { Text(textoBoton, style = MaterialTheme.typography.bodySmall) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Horarios disponibles:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            if (horariosDisponibles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay horarios disponibles para esta fecha.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(horariosDisponibles) { hora ->
                        FilterChip(
                            selected = horaSeleccionada == hora,
                            onClick = { horaSeleccionada = hora },
                            label = { Text(hora) }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (horaSeleccionada != null) {
                        onContinuar(fechaIso, horaSeleccionada!!)
                    }
                },
                enabled = horaSeleccionada != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Continuar")
            }
        }
    }
}

/**
 * Genera los próximos 5 días hábiles (excluyendo sábados y domingos)
 */
private fun obtenerProximosDiasHabiles(semanaOffset: Int): List<LocalDate> {
    var fechaInicio = LocalDate.now().plusWeeks(semanaOffset.toLong())

    while (fechaInicio.dayOfWeek == DayOfWeek.SATURDAY || fechaInicio.dayOfWeek == DayOfWeek.SUNDAY) {
        fechaInicio = fechaInicio.plusDays(1)
    }

    val dias = mutableListOf<LocalDate>()
    var actual = fechaInicio

    while (dias.size < 5) {
        if (actual.dayOfWeek != DayOfWeek.SATURDAY && actual.dayOfWeek != DayOfWeek.SUNDAY) {
            dias.add(actual)
        }
        actual = actual.plusDays(1)
    }
    return dias
}