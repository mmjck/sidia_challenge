package com.mmjck.movies.data.repository

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import retrofit2.Response

interface IMovies {
    suspend fun get() : Response<MoviesResponse>
    suspend fun getById(id: String) : Response<Movie>
    suspend fun search(title: String) : Response<MoviesResponse>
}