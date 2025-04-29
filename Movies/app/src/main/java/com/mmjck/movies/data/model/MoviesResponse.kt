package com.mmjck.movies.data.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse (
    @SerializedName("data")
    val data: List<Movie>,

    @SerializedName("pagination")
    val page: Pagination
)