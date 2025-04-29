package com.mmjck.movies.data.model

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("total")
    val total: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("pages")
    val pages: Int
)