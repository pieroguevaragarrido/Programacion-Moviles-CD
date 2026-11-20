package com.guevara.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.guevara.centrotareas.model.CargaUi
import com.guevara.centrotareas.model.PrimosUi
import com.guevara.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    // "Tócame" es la prueba de vida: si deja de sumar, la pantalla está congelada
    var toques by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Button(
                onClick = { toques++ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text("Tócame: $toques")
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaProgreso(vm)
                TarjetaPrimos(vm)
                TarjetaCargas(vm)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TarjetaProgreso(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()

    TarjetaBase("Tarea con progreso") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { vm.tareaLarga() },
                enabled = !corriendo
            ) {
                Text("Iniciar")
            }
            OutlinedButton(
                onClick = { vm.cancelar() },
                enabled = corriendo
            ) {
                Text("Cancelar")
            }
        }
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier.fillMaxWidth()
        )
        Text("${(progreso * 100).toInt()} %")
        Text(mensaje)
    }
}

@Composable
private fun TarjetaPrimos(vm: TareasViewModel) {
    val estado by vm.primos.collectAsState()
    // Valor por defecto: ajústalo si en tu emulador tarda menos de 2 s o más de 5 s
    var texto by rememberSaveable { mutableStateOf("500000") }
    val calculando = estado is PrimosUi.Calculando

    TarjetaBase("Contar números primos") {
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Contar primos hasta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = !calculando,
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    // Se ignoran los textos no numéricos o menores que 2
                    val n = texto.toIntOrNull()
                    if (n != null && n >= 2) vm.contar(n)
                },
                enabled = !calculando
            ) {
                Text("Contar")
            }
            OutlinedButton(
                onClick = { vm.cancelarPrimos() },
                enabled = calculando
            ) {
                Text("Cancelar")
            }
        }
        when (val e = estado) {
            PrimosUi.Inicial -> Unit
            PrimosUi.Calculando -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Text("Calculando...")
            }
            is PrimosUi.Listo -> CuadroResultado(
                "Primos encontrados: ${e.cantidad}\nTiempo: ${e.milisegundos} ms"
            )
            PrimosUi.Cancelado -> Text("Cancelado")
        }
    }
}

@Composable
private fun TarjetaCargas(vm: TareasViewModel) {
    val carga by vm.carga.collectAsState()
    val cargando = carga is CargaUi.Cargando

    TarjetaBase("Dos cargas") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { vm.cargarUnaPorUna() },
                enabled = !cargando
            ) {
                Text("Una por una")
            }
            Button(
                onClick = { vm.cargarALaVez() },
                enabled = !cargando
            ) {
                Text("A la vez")
            }
        }
        when (val c = carga) {
            CargaUi.Inicial -> Unit
            CargaUi.Cargando -> CircularProgressIndicator()
            is CargaUi.Exito -> CuadroResultado(
                "Modo: ${c.modo}\nPerfil: ${c.perfil}\nPedidos: ${c.pedidos}\nTiempo: ${c.milisegundos} ms"
            )
            is CargaUi.Error -> Text(c.mensaje)
        }
    }
}

// ---------- Componentes auxiliares ----------

@Composable
private fun TarjetaBase(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}

@Composable
private fun CuadroResultado(texto: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE8F5E9))
            .padding(12.dp)
    ) {
        Text(text = texto, color = Color(0xFF1B5E20))
    }
}

@Composable
private fun CuadroError(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = mensaje, color = MaterialTheme.colorScheme.onErrorContainer)
        Button(
            onClick = onReintentar,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Reintentar")
        }
    }
}
