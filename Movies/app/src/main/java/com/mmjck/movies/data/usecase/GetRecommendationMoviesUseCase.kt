package com.mmjck.movies.data.usecase

import com.mmjck.movies.data.impl.FavoritesRepositoriesImpl
import com.mmjck.movies.data.impl.MovieRepositoriesImpl
import com.mmjck.movies.data.impl.RecommendationRepositoriesImpl
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRecommendationMoviesUseCase @Inject constructor(private val repository: RecommendationRepositoriesImpl) {
    operator fun invoke() = flow<NetworkResult<MoviesResponse>>{
        emit(NetworkResult.Loading(true))

        val result = repository.get()
        if (!result.isSuccessful) {
            emit(NetworkResult.Failure(message = result.message()))
        }
        result.body()?.let { body ->
            emit(NetworkResult.Success(data = body))
        }
    }.catch {
        emit(NetworkResult.Failure(message = it.message.toString()))
    }.flowOn(Dispatchers.IO)
}