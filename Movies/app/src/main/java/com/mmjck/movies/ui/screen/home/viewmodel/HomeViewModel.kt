package com.mmjck.movies.ui.screen.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.data.model.MoviesResponse
import com.mmjck.movies.data.usecase.GetFavoritesMoviesUseCase
import com.mmjck.movies.data.usecase.GetMoviesUseCase
import com.mmjck.movies.data.usecase.GetRecommendationMoviesUseCase
import com.mmjck.movies.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: GetRecommendationMoviesUseCase) :
    ViewModel() {

    private val  _data = MutableLiveData<NetworkResult<MoviesResponse>>()
    val data: LiveData<NetworkResult<MoviesResponse>>
        get() = _data

    fun getData() = viewModelScope.launch{
        useCase.invoke().collectLatest { result ->
            _data.value = result
         }
    }
}