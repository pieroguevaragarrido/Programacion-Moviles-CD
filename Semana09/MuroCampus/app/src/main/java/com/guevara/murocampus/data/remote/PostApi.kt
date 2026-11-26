package com.guevara.murocampus.data.remote

import com.guevara.murocampus.model.Comentario
import com.guevara.murocampus.model.NuevoPost
import com.guevara.murocampus.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/** Describe las llamadas del servicio JSONPlaceholder. Las rutas no empiezan con "/". */
interface PostApi {

    @GET("posts")
    suspend fun listar(): List<Post>

    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post
}
