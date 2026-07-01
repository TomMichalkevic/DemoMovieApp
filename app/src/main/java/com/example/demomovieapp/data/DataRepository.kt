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
        .baseUrl(BuildConfig.TMDB_BASE_URL)
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

    suspend fun searchMovies(query: String, page: Int = 1): Pair<List<Movie>, Int> = withContext(Dispatchers.IO) {
        val response = tmdbApi.searchMovies(apiKey, query, page = page)
        Pair(response.results.map { mapToDomain(it) }, response.totalPages)
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
            val hasTrailer = response.results.any { it.type.equals(VideoType.TRAILER.value, ignoreCase = true) }
            if (hasTrailer) {
                // Return local sample video resource ID
                com.example.demomovieapp.R.raw.sample_video.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
