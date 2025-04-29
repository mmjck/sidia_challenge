package com.mmjck.movies.ui.screen.favorites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.usecase.GetMoviesByIdUseCase
import com.mmjck.movies.data.usecase.GetRecommendationMoviesByIdUseCase
import com.mmjck.movies.data.usecase.UpdateFavoritesMoviesUseCase
import com.mmjck.movies.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getMoviesByIdUseCase: GetMoviesByIdUseCase,
    private val getRecommendationMoviesByIdUseCase: GetRecommendationMoviesByIdUseCase,
    private val updateFavoritesUseCase: UpdateFavoritesMoviesUseCase
    ) :
    ViewModel() {


    private val _moviesRecommendation = MutableLiveData<NetworkResult<MoviesResponse>>()
    val moviesRecommendation: LiveData<NetworkResult<MoviesResponse>>
        get() = _moviesRecommendation

    private val  _data = MutableLiveData<NetworkResult<Movie>>()
    val data: LiveData<NetworkResult<Movie>>
        get() = _data




    fun getData(id: String) = viewModelScope.launch{
        getMoviesByIdUseCase.invoke(id).collectLatest {
            _data.value = it
        }

        getRecommendationMoviesByIdUseCase.invoke(id).collectLatest {
            _moviesRecommendation.value = it
        }
    }


    fun updateFavorites(id: String): Flow<NetworkResult<Unit>> {
        return updateFavoritesUseCase.invoke(id)
    }
}