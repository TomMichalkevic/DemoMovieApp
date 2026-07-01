package com.example.demomovieapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.MovieRepository
import com.example.demomovieapp.data.remote.api.TmdbApi
import com.example.demomovieapp.data.remote.dto.VideoType
import com.example.demomovieapp.data.remote.mapper.toDomain

class MovieRepositoryImpl(
    private val tmdbApi: TmdbApi
) : MovieRepository {

    override suspend fun getPopular(): List<Movie> = withContext(Dispatchers.IO) {
        tmdbApi.getPopular().results.map { it.toDomain() }
    }

    override suspend fun getTopRated(): List<Movie> = withContext(Dispatchers.IO) {
        tmdbApi.getTopRated().results.map { it.toDomain() }
    }

    override suspend fun searchMovies(query: String, page: Int): Pair<List<Movie>, Int> = withContext(Dispatchers.IO) {
        val response = tmdbApi.searchMovies(query = query, page = page)
        Pair(response.results.map { it.toDomain() }, response.totalPages)
    }

    override suspend fun getTrailerUrl(movieId: Int): String? = withContext(Dispatchers.IO) {
        try {
            val response = tmdbApi.getVideos(movieId = movieId)
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
