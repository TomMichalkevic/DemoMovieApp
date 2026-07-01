package com.example.demomovieapp.data

import com.example.demomovieapp.BuildConfig
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

    private val tmdbApi = tmdbRetrofit.create(TmdbApi::class.java)

    private val apiKey = BuildConfig.TMDB_API_KEY

    suspend fun getPopular(): List<Movie> = withContext(Dispatchers.IO) {
        tmdbApi.getPopular(apiKey).results.map { mapToDomain(it) }
    }

    suspend fun getTopRated(): List<Movie> = withContext(Dispatchers.IO) {
        tmdbApi.getTopRated(apiKey).results.map { mapToDomain(it) }
    }

    suspend fun searchMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        tmdbApi.searchMovies(apiKey, query).results.map { mapToDomain(it) }
    }

    private fun mapToDomain(tmdbMovie: TmdbMovie): Movie {
        return Movie(
            id = tmdbMovie.id,
            title = tmdbMovie.title,
            overview = tmdbMovie.overview,
            posterUrl = tmdbMovie.posterUrl,
            backdropUrl = tmdbMovie.backdropUrl,
            voteAverage = tmdbMovie.voteAverage,
            trailerUrl = null
        )
    }

    suspend fun getTrailerUrl(movieId: Int): String? = withContext(Dispatchers.IO) {
        try {
            val response = tmdbApi.getVideos(movieId, apiKey)
            val hasTrailer = response.results.any { it.type.equals("Trailer", ignoreCase = true) }
            if (hasTrailer) {
                // Return a high-quality sample video to natively demonstrate ExoPlayer
                "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
