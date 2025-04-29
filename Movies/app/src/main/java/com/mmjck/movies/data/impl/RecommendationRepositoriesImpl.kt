package com.mmjck.movies.data.impl

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.model.UpdateFavoritesRequest
import com.mmjck.movies.data.repository.IFavoritesMovies
import com.mmjck.movies.data.repository.IRecommendation
import com.mmjck.movies.network.FavoritesService
import com.mmjck.movies.network.RecommendationService
import retrofit2.Response
import javax.inject.Inject

class RecommendationRepositoriesImpl @Inject constructor(private val service: RecommendationService ) : IRecommendation {
    override suspend fun get(): Response<MoviesResponse> {
        return this.service.get()
    }

    override suspend fun getById(id: String): Response<MoviesResponse> {
        return this.service.getById(id)
    }
}