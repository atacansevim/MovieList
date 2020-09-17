package com.example.movielist.model

import com.google.gson.annotations.SerializedName

data class Genres(// FOR GENRE API
    @SerializedName("genres")
    val genres: List<Genre>
)