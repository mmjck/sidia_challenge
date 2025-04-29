package com.mmjck.movies.data.repository

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.model.UpdateFavoritesRequest
import retrofit2.Response

interface IFavoritesMovies {
    suspend fun getMovies() : Response<MoviesResponse>
    suspend fun markAsFavorite(data: UpdateFavoritesRequest) : Response<Unit>
}