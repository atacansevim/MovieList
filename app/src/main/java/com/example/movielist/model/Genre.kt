package com.example.movielist.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Genre (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String

): Serializable// Serializable for pass object array between 2 activity

