package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.data.local.ItemDao
import com.tecsup.pokemon.data.mapper.aEntity
import com.tecsup.pokemon.data.mapper.aModelo
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.data.remote.ConfigApi
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/**
 * Room es la fuente única de verdad: la UI siempre lee de Room.
 * Retrofit solo descarga y actualiza la tabla.
 */
class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao,
    private val tipo: String = ConfigApi.TIPO
) : ItemRepository {

    override val items: Flow<List<Item>> =
        dao.todos().map { lista -> lista.map { it.aModelo() } }

    override val favoritos: Flow<List<Item>> =
        dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        // Si se hace upsert sin cuidado, esFavorito vuelve a false y se pierden los favoritos.
        // Por eso se leen antes y se vuelven a aplicar.
        val favoritosActuales = dao.idsFavoritos().toSet()
        val nuevos = api.listar(tipo).pokemon          // la API devuelve un envoltorio
            .map { it.pokemon }
            .map { dto -> dto.aEntity(esFavorito = dto.id in favoritosActuales) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {            // sin internet / timeout
        Result.failure(e)
    } catch (e: HttpException) {          // 404, 500...
        Result.failure(e)
    } catch (e: SerializationException) { // JSON inesperado
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        // El segundo endpoint es /pokemon/{nombre}: el nombre se toma de Room.
        val guardado = dao.porId(id)
        val nombre = guardado?.titulo ?: id.toString()
        val dto = api.detalle(nombre)
        Result.success(dto.aModelo(esFavorito = guardado?.esFavorito ?: false))
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: SerializationException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)

    override suspend fun cantidadGuardada(): Int = dao.contar()

    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()

    override suspend fun limpiarCache() = dao.borrarTodo()
}
