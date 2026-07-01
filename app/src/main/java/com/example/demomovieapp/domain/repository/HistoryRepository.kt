package com.example.demomovieapp.domain.repository

import com.example.demomovieapp.domain.model.Movie
import kotlinx.coroutines.flow.StateFlow

interface HistoryRepository {
    val viewedMovies: StateFlow<List<Movie>>
    fun addViewedMovie(movie: Movie)
}
