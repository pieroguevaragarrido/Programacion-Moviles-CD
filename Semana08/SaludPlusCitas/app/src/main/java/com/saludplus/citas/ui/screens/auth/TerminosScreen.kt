package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.TopBarSaludPlus

@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Términos y Condiciones", onVolver = onVolver) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Términos de Servicio SaludPlus", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = """
                    Bienvenido a la aplicación Paciente de Clínica SaludPlus. Al utilizar nuestra plataforma, usted acepta los siguientes términos:

                    1. Protección de Datos Personal: Sus datos de salud son confidenciales y están protegidos según las normas vigentes.

                    2. Cancelación de Citas: Puede cancelar sus citas con al menos 2 horas de anticipación desde el módulo 'Mis Citas'.

                    3. Tolerancia de Espera: Le recomendamos llegar 10 minutos antes de la hora programada para su consulta.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}