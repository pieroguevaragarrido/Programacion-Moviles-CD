package com.example.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.registronotas.ui.theme.RegistroNotasTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroNotasTheme {
                PantallaNotas()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNotas() {
    var notaFundamentos by remember { mutableStateOf(0f) }
    var notaPOO by remember { mutableStateOf(0f) }
    var notaMoviles by remember { mutableStateOf(0f) }
    var notaBD by remember { mutableStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Notas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            FilaCurso("Fundamentos de Programación", "20%", notaFundamentos) { notaFundamentos = it }
            Spacer(modifier = Modifier.height(16.dp))

            FilaCurso("Programación Orientada a Objetos", "25%", notaPOO) { notaPOO = it }
            Spacer(modifier = Modifier.height(16.dp))

            FilaCurso("Programación en Móviles", "30%", notaMoviles) { notaMoviles = it }
            Spacer(modifier = Modifier.height(16.dp))

            FilaCurso("Base de Datos", "25%", notaBD) { notaBD = it }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Redondear promedio final")
                Switch(checked = redondear, onCheckedChange = { redondear = it })
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = confirmado, onCheckedChange = { confirmado = it })
                Text("Confirmo que las notas son correctas")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { mostrarResultado = true },
                enabled = confirmado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CALCULAR PROMEDIO")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (!mostrarResultado) {
                Text(
                    text = "Asigna las notas y confirma para calcular",
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                val promedioPonderado = notaFundamentos * 0.20f +
                        notaPOO * 0.25f +
                        notaMoviles * 0.30f +
                        notaBD * 0.25f

                val promedioFinal = if (redondear) {
                    promedioPonderado.roundToInt().toFloat()
                } else {
                    promedioPonderado
                }

                val (observacion, colorChip) = when {
                    promedioFinal >= 17f -> "EXCELENTE" to Color(0xFF1B5E20)
                    promedioFinal >= 13f -> "APROBADO" to Color(0xFF4CAF50)
                    promedioFinal >= 10f -> "EN RECUPERACIÓN" to Color(0xFFFFA000)
                    else -> "DESAPROBADO" to Color(0xFFD32F2F)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Promedio ponderado: " + String.format("%.2f", promedioPonderado),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Promedio final: " + String.format("%.2f", promedioFinal) +
                                    if (redondear) " (redondeado)" else "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(colorChip, shape = MaterialTheme.shapes.small)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = observacion, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✓ Promedio calculado correctamente",
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Desarrollado por: Piero Guevara",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun FilaCurso(nombre: String, peso: String, nota: Float, onNotaChange: (Float) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$nombre ($peso)", style = MaterialTheme.typography.bodyLarge)
            Box(
                modifier = Modifier
                    .background(
                        if (nota < 13f) Color(0xFFD32F2F) else Color(0xFF4CAF50),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = nota.toInt().toString(), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19
        )
    }
}