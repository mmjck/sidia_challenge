package com.mmjck.movies.data.model

import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String
)