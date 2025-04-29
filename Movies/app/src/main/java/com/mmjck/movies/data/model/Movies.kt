package com.mmjck.movies.data.model

import com.google.gson.annotations.SerializedName

import android.os.Parcel
import android.os.Parcelable

data class  Movie(
    @SerializedName("_id")
    val id: String,

    @SerializedName("tmdb_id")
    val tmdbId: Int,

    @SerializedName("isFavorite")
    val isFavorite: Boolean = false,

    @SerializedName("movie_id")
    val movieId: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("budget")
    val budget: Int,

    @SerializedName("revenue")
    val revenue: Float,

    @SerializedName("runtime")
    val runtime: Int,

    @SerializedName("vote_average")
    val voteAverage: Float?,

    @SerializedName("vote_count")
    val voteCount: Int,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("path")
    val path: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("genres")
    val genres: List<Genre>,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,

    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,

    @SerializedName("created_at")
    val createdAt: String,
)