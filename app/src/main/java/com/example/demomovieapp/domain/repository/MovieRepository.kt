package com.example.demomovieapp.domain.repository

import com.example.demomovieapp.domain.model.Movie

interface MovieRepository {
    suspend fun getPopular(): List<Movie>
    suspend fun getTopRated(): List<Movie>
    suspend fun searchMovies(query: String, page: Int = 1): Pair<List<Movie>, Int>
    suspend fun getTrailerUrl(movieId: Int): String?
}
