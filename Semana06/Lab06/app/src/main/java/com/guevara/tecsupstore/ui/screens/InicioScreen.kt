package com.guevara.tecsupstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guevara.tecsupstore.data.categorias
import com.guevara.tecsupstore.data.secciones
import com.guevara.tecsupstore.ui.components.TarjetaProducto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onProductoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var categoriaSeleccionada by remember { mutableStateOf("Todo") }

    val seccionesVisibles = if (categoriaSeleccionada == "Todo") {
        secciones
    } else {
        secciones.filter { it.titulo == categoriaSeleccionada }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Lab 4: LazyRow de categorías
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                FilterChip(
                    selected = categoria == categoriaSeleccionada,
                    onClick = { categoriaSeleccionada = categoria },
                    label = { Text(categoria) }
                )
            }
        }

        // Lab 4: LazyColumn de secciones con productos
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            seccionesVisibles.forEach { seccion ->
                item {
                    Text(
                        text = seccion.titulo,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(seccion.productos) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        onClick = { onProductoClick(producto.id) }
                    )
                }
            }
        }
    }
}
