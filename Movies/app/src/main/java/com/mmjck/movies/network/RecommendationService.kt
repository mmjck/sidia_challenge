package com.mmjck.movies.network;

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query;

interface RecommendationService {
    @GET("recommendations")
    suspend fun get() : Response<MoviesResponse>

    @GET("recommendations/{id}")
    suspend fun getById(@Path("id") id: String): Response<MoviesResponse>

    @GET("recommendations-related-to-id")
    suspend fun search(@Query("q") q: String): Response<MoviesResponse>
}