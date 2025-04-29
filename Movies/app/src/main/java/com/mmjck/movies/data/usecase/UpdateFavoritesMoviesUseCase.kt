package com.mmjck.movies.data.usecase

import android.util.Log
import com.mmjck.movies.data.impl.FavoritesRepositoriesImpl
import com.mmjck.movies.data.impl.MovieRepositoriesImpl
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.model.UpdateFavoritesRequest
import com.mmjck.movies.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateFavoritesMoviesUseCase @Inject constructor(private val repository: FavoritesRepositoriesImpl) {
    operator fun invoke(movieId: String) = flow<NetworkResult<Unit>>{
        emit(NetworkResult.Loading(true))

        // userId as default 1
        val data = UpdateFavoritesRequest(movieId = movieId)

        val result = repository.markAsFavorite(data)

        if (!result.isSuccessful) {
            emit(NetworkResult.Failure(message = result.message()))
        }

        emit(NetworkResult.Success(Unit))
    }.catch {
        emit(NetworkResult.Failure(message = it.message.toString()))
    }.flowOn(Dispatchers.IO)
}