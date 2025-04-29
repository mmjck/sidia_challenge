package com.mmjck.movies.network;

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query;

interface MoviesService {
    @GET("movies")
    suspend fun get() : Response<MoviesResponse>

    @GET("movies/{id}")
    suspend fun getById(@Path("id") id: String): Response<Movie>

    @GET("movies/search")
    suspend fun search(@Query("q") q: String): Response<MoviesResponse>
}