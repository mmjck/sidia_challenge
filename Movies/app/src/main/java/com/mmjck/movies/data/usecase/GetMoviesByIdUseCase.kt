package com.mmjck.movies.data.usecase

import com.mmjck.movies.data.impl.MovieRepositoriesImpl
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMoviesByIdUseCase @Inject constructor(private val repository: MovieRepositoriesImpl) {
    operator fun invoke(id: String) = flow<NetworkResult<Movie>>{
        emit(NetworkResult.Loading(true))

        val result = repository.getById(id)
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
