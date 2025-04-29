package com.mmjck.movies.data.impl

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.repository.IMovies
import com.mmjck.movies.network.MoviesService
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoriesImpl @Inject constructor(private val service: MoviesService ) : IMovies {
    override suspend fun get(): Response<MoviesResponse> {
        return this.service.get()
    }

    override suspend fun getById(id: String): Response<Movie> {
        return this.service.getById(id)
    }

    override suspend fun search(title: String): Response<MoviesResponse> {
        return this.service.search(title)
    }
}