package com.mmjck.movies.di

import com.mmjck.movies.network.FavoritesService
import com.mmjck.movies.network.MoviesService
import com.mmjck.movies.network.RecommendationService
import com.mmjck.movies.utils.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = ApiConstants.BASE_URL

    @Provides
    fun client(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): FavoritesService = retrofit.create(FavoritesService::class.java)

    @Provides
    @Singleton
    fun provideMoviesService(retrofit: Retrofit): MoviesService = retrofit.create(MoviesService::class.java)

    @Provides
    @Singleton
    fun provideRecommendationService(retrofit: Retrofit): RecommendationService = retrofit.create(RecommendationService::class.java)

}