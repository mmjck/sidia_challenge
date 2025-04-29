package com.mmjck.movies.data.impl

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.model.UpdateFavoritesRequest
import com.mmjck.movies.data.repository.IFavoritesMovies
import com.mmjck.movies.network.FavoritesService
import retrofit2.Response
import javax.inject.Inject

class FavoritesRepositoriesImpl @Inject constructor(private val service: FavoritesService ) : IFavoritesMovies {
    override suspend fun getMovies(): Response<MoviesResponse> {
        return this.service.get()
    }

    override suspend fun markAsFavorite(data: UpdateFavoritesRequest): Response<Unit> {
        return this.service.updateFavorites(data)
    }
}