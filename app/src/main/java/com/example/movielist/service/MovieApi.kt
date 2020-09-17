package com.example.movielist.service

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "adc794164ac343979c4a9e2d90fc99f0",
        @Query("page") page: Int
    ): retrofit2.Call<com.example.movielist.model.Movie>

}