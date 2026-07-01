package com.example.demomovieapp.data

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): TmdbResponse
}

interface ItunesApi {
    @GET("search")
    suspend fun searchMovie(
        @Query("term") term: String,
        @Query("entity") entity: String = "movie",
        @Query("limit") limit: Int = 1
    ): ItunesResponse
}
