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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.registronotas.ui.theme.RegistroNotasTheme

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
                    .background(Color(0xFF4CAF50), shape = MaterialTheme.shapes.small)
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