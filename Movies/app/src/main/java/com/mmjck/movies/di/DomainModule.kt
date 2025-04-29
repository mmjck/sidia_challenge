package com.mmjck.movies.di

import com.mmjck.movies.data.impl.FavoritesRepositoriesImpl
import com.mmjck.movies.data.impl.MovieRepositoriesImpl
import com.mmjck.movies.data.impl.RecommendationRepositoriesImpl
import com.mmjck.movies.data.repository.IFavoritesMovies
import com.mmjck.movies.data.repository.IMovies
import com.mmjck.movies.data.repository.IRecommendation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsImpl(
        favoritesMovies: FavoritesRepositoriesImpl
    ): IFavoritesMovies


    @Binds
    abstract fun bindsMoviesImpl(
        movieRepositoriesImpl: MovieRepositoriesImpl
    ): IMovies


    @Binds
    abstract fun bindsRecommendationsImpl(
        d: RecommendationRepositoriesImpl
    ): IRecommendation
}