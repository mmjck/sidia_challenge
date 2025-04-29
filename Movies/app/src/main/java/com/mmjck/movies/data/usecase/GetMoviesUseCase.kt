package com.mmjck.movies.data.usecase

import android.util.Log
import com.mmjck.movies.data.impl.MovieRepositoriesImpl
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.repository.IRecommendation
import com.mmjck.movies.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: IRecommendation) {
    operator fun invoke() = flow<NetworkResult<MoviesResponse>>{
        emit(NetworkResult.Loading(true))

        val response = repository.get()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(NetworkResult.Success(it))
            } ?: emit(NetworkResult.Failure("Response body is null"))
        } else {
            emit(NetworkResult.Failure("Error: ${response.code()} - ${response.message()}"))
        }

        response.body()?.let { body ->
            //val response = body.map { it.toDomain() }
            emit(NetworkResult.Success(data = body))
        }
    }.catch { e ->
        emit(NetworkResult.Failure("Exception: ${e.message}"))
    }.flowOn(Dispatchers.IO)
}
