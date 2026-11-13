package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.R
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.theme.NaranjaFuego
import com.tecsup.pokemon.ui.theme.RojoOscuro
import com.tecsup.pokemon.ui.theme.RojoPokedex

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIngreso: (Sesion) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val errorCorreo by viewModel.errorCorreo.collectAsStateWithLifecycle()
    val errorClave by viewModel.errorClave.collectAsStateWithLifecycle()

    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var verClave by rememberSaveable { mutableStateOf(false) }

    // Cuando el login es correcto, se avisa hacia arriba (la navegación no es asunto del ViewModel)
    LaunchedEffect(estado) {
        val actual = estado
        if (actual is UiState.Exito) onIngreso(actual.datos)
    }

    val cargando = estado is UiState.Cargando
    // El botón se deshabilita mientras los campos estén vacíos
    val botonHabilitado = !cargando && correo.isNotBlank() && clave.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(RojoPokedex, RojoOscuro, NaranjaFuego)))
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_pokeball),
                contentDescription = null,
                modifier = Modifier.size(110.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text("Pokédex Fuego", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Text(
                "Ingresa para ver a los Pokémon de tipo fuego",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            viewModel.limpiarErrores()
                        },
                        label = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        singleLine = true,
                        enabled = !cargando,
                        isError = errorCorreo != null,
                        supportingText = errorCorreo?.let { err ->
                            { Text(err, color = MaterialTheme.colorScheme.error) }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = clave,
                        onValueChange = {
                            clave = it
                            viewModel.limpiarErrores()
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            TextButton(onClick = { verClave = !verClave }) {
                                Text(if (verClave) "Ocultar" else "Ver")
                            }
                        },
                        singleLine = true,
                        enabled = !cargando,
                        isError = errorClave != null,
                        supportingText = errorClave?.let { err ->
                            { Text(err, color = MaterialTheme.colorScheme.error) }
                        },
                        visualTransformation = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    val generalError = (estado as? UiState.Error)?.mensaje
                    if (generalError != null && errorCorreo == null && errorClave == null) {
                        Text(
                            generalError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Button(
                        onClick = { viewModel.ingresar(correo, clave) },
                        enabled = botonHabilitado,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text("Ingresar")
                        }
                    }
                }
            }
        }
    }
}
