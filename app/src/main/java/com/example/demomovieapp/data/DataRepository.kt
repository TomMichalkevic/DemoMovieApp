package com.example.demomovieapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MovieRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val mediaType = "application/json".toMediaType()

    private val tmdbRetrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(json.asConverterFactory(mediaType))
        .build()

    private val itunesRetrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(json.asConverterFactory(mediaType))
        .build()

    private val tmdbApi = tmdbRetrofit.create(TmdbApi::class.java)
    private val itunesApi = itunesRetrofit.create(ItunesApi::class.java)

    // TODO: Add your TMDB API key here before running
    private val TMDB_API_KEY = "YOUR_API_KEY_HERE"

    suspend fun getNowPlaying(): List<Movie> = withContext(Dispatchers.IO) {
        val response = tmdbApi.getNowPlaying(TMDB_API_KEY)
        response.results.map { tmdbMovie ->
            Movie(
                id = tmdbMovie.id,
                title = tmdbMovie.title,
                overview = tmdbMovie.overview,
                posterUrl = tmdbMovie.posterUrl,
                backdropUrl = tmdbMovie.backdropUrl,
                voteAverage = tmdbMovie.voteAverage,
                trailerUrl = null // Fetched on-demand
            )
        }
    }

    suspend fun getTrailerUrl(movieTitle: String): String? = withContext(Dispatchers.IO) {
        try {
            val response = itunesApi.searchMovie(movieTitle)
            response.results.firstOrNull()?.previewUrl
        } catch (e: Exception) {
            null
        }
    }
}
