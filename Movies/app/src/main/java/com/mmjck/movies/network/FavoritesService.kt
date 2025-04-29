package com.mmjck.movies.network;

import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.model.UpdateFavoritesRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET;
import retrofit2.http.POST
import retrofit2.http.Query;

interface FavoritesService {
    @GET("/movies/favorites")
    suspend fun get(
        @Query("userId") userId: Int = 1
    ) : Response<MoviesResponse>


    @POST("/movies/favorites")
    suspend fun updateFavorites(
        @Body body: UpdateFavoritesRequest
    ): Response<Unit>
}