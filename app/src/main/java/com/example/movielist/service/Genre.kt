package com.example.movielist.service


import retrofit2.http.GET
import retrofit2.http.Query

interface Genre {
    @GET("genre/movie/list")
    fun getGenreName(
        @Query("api_key") apiKey: String = "adc794164ac343979c4a9e2d90fc99f0"
    ): retrofit2.Call<com.example.movielist.model.Genres>
}