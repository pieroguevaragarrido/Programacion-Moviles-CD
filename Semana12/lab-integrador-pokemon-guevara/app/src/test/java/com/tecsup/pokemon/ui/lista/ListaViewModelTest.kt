package com.tecsup.pokemon.ui.lista

import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

/**
 * Regla de JUnit para reemplazar Dispatchers.Main por un TestDispatcher en las pruebas unitarias.
 * Es indispensable porque ListaViewModel utiliza viewModelScope (el cual usa Dispatchers.Main por defecto).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

/**
 * Fake manual de ItemRepository para las pruebas unitarias sin frameworks de mocking.
 * Permite configurar el Flow de items, el resultado de refrescar() y la cantidad de elementos guardados.
 */
class FakeItemRepository(
    val itemsFlow: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList()),
    var resultadoRefrescar: Result<Int> = Result.success(0),
    var cantidadGuardadaVal: Int = 0
) : ItemRepository {

    override val items: Flow<List<Item>> = itemsFlow
    override val favoritos: Flow<List<Item>> = MutableStateFlow(emptyList())

    override suspend fun refrescar(): Result<Int> = resultadoRefrescar

    override suspend fun obtenerDetalle(id: Int): Result<Item> =
        Result.failure(Exception("No implementado"))

    override suspend fun alternarFavorito(id: Int) {}

    override suspend fun cantidadGuardada(): Int = cantidadGuardadaVal

    override suspend fun cantidadFavoritos(): Int = 0

    override suspend fun limpiarCache() {}
}

/**
 * Estructura para registrar los mensajes enviados al Notificador.
 */
data class NotificacionEnviada(val titulo: String, val texto: String)

/**
 * Fake manual de Notificador que guarda en una lista las notificaciones recibidas.
 */
class FakeNotificador : Notificador {
    val notificaciones = mutableListOf<NotificacionEnviada>()

    override fun mostrar(titulo: String, texto: String) {
        notificaciones.add(NotificacionEnviada(titulo, texto))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val pokemonPikachu = Item(
        id = 25,
        titulo = "Pikachu",
        subtitulo = "#025",
        imagenUrl = "https://example.com/pikachu.png",
        esFavorito = true
    )

    private val pokemonCharmander = Item(
        id = 4,
        titulo = "Charmander",
        subtitulo = "#004",
        imagenUrl = "https://example.com/charmander.png",
        esFavorito = false
    )

    // Pruebas unitarias solicitadas:

    // 1. refrescar con éxito: el estado termina en UiState.Exito con la lista y el notificador recibe "Se guardaron N elementos".
    @Test
    fun refrescarExitoso_actualizaEstadoAExitoYNotificaCantidad() = runTest {
        val listaItems = listOf(pokemonPikachu)
        val fakeRepo = FakeItemRepository(
            itemsFlow = MutableStateFlow(listaItems),
            resultadoRefrescar = Result.success(1),
            cantidadGuardadaVal = 1
        )
        val fakeNotificador = FakeNotificador()

        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        // Verificamos que el estado sea Exito con la lista devuelta por el repositorio
        val estadoActual = viewModel.estado.value
        assertTrue(estadoActual is UiState.Exito)
        assertEquals(listaItems, (estadoActual as UiState.Exito).datos)

        // Verificamos que el notificador recibió el mensaje esperado
        assertEquals(1, fakeNotificador.notificaciones.size)
        assertEquals("Pokédex actualizada", fakeNotificador.notificaciones[0].titulo)
        assertEquals("Se guardaron 1 elementos", fakeNotificador.notificaciones[0].texto)
    }

    // 2. refrescar falla y no hay nada guardado: el estado es UiState.Error.
    @Test
    fun refrescarFalla_sinDatosGuardados_cambiaEstadoAError() = runTest {
        val fakeRepo = FakeItemRepository(
            itemsFlow = MutableStateFlow(emptyList()),
            resultadoRefrescar = Result.failure(IOException("Sin red")),
            cantidadGuardadaVal = 0
        )
        val fakeNotificador = FakeNotificador()

        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        // Verificamos que el estado sea Error cuando refrescar falla y no hay items guardados
        val estadoActual = viewModel.estado.value
        assertTrue(estadoActual is UiState.Error)
        assertEquals("No hay conexión a internet", (estadoActual as UiState.Error).mensaje)
    }

    // 3. refrescar falla pero hay datos guardados: se muestra la lista y aviso no es null.
    @Test
    fun refrescarFalla_conDatosGuardados_muestraListaYAvisoNoNull() = runTest {
        val listaItems = listOf(pokemonPikachu)
        val fakeRepo = FakeItemRepository(
            itemsFlow = MutableStateFlow(listaItems),
            resultadoRefrescar = Result.failure(IOException("Sin red")),
            cantidadGuardadaVal = 1
        )
        val fakeNotificador = FakeNotificador()

        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        // Verificamos que el estado mantenga los datos guardados en UiState.Exito
        val estadoActual = viewModel.estado.value
        assertTrue(estadoActual is UiState.Exito)
        assertEquals(listaItems, (estadoActual as UiState.Exito).datos)

        // Verificamos que aviso contenga la advertencia de sin conexión
        assertNotNull(viewModel.aviso.value)
        assertEquals("Sin conexión: mostrando datos guardados", viewModel.aviso.value)
    }

    // 4. refrescar falla: el notificador NO recibe ninguna notificación.
    @Test
    fun refrescarFalla_noEnviaNingunaNotificacion() = runTest {
        val fakeRepo = FakeItemRepository(
            itemsFlow = MutableStateFlow(emptyList()),
            resultadoRefrescar = Result.failure(IOException("Error de red")),
            cantidadGuardadaVal = 0
        )
        val fakeNotificador = FakeNotificador()

        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        // Verificamos que el estado sea Error y la lista de notificaciones enviadas esté vacía tras un fallo
        assertTrue(viewModel.estado.value is UiState.Error)
        assertTrue(fakeNotificador.notificaciones.isEmpty())
    }

    // 5. búsqueda por nombre o número filtra correctamente los Pokémon.
    @Test
    fun busqueda_filtraPorNombreONumeroCorrectamente() = runTest {
        val listaItems = listOf(pokemonPikachu, pokemonCharmander)
        val fakeRepo = FakeItemRepository(
            itemsFlow = MutableStateFlow(listaItems),
            resultadoRefrescar = Result.success(2),
            cantidadGuardadaVal = 2
        )
        val fakeNotificador = FakeNotificador()

        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        // Buscar por nombre (sensible a minúsculas/mayúsculas)
        viewModel.onBusquedaCambio("pika")
        advanceUntilIdle()
        var estado = viewModel.estado.value as UiState.Exito
        assertEquals(1, estado.datos.size)
        assertEquals("Pikachu", estado.datos[0].titulo)

        // Buscar por número (#004)
        viewModel.onBusquedaCambio("#004")
        advanceUntilIdle()
        estado = viewModel.estado.value as UiState.Exito
        assertEquals(1, estado.datos.size)
        assertEquals("Charmander", estado.datos[0].titulo)

        // Buscar término que no coincide
        viewModel.onBusquedaCambio("Mewtwo")
        advanceUntilIdle()
        estado = viewModel.estado.value as UiState.Exito
        assertTrue(estado.datos.isEmpty())
    }

    // 6. contadores de total de Pokémon y favoritos se actualizan correctamente.
    @Test
    fun contadores_reflejanTotalYFavoritos() = runTest {
        val listaItems = listOf(pokemonPikachu, pokemonCharmander)
        val fakeRepo = FakeItemRepository(
            itemsFlow = MutableStateFlow(listaItems),
            resultadoRefrescar = Result.success(2),
            cantidadGuardadaVal = 2
        )
        val fakeNotificador = FakeNotificador()

        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        assertEquals(2, viewModel.totalPokemon.value)
        assertEquals(1, viewModel.totalFavoritos.value)
    }
}
