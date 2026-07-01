package com.example.demomovieapp.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.HistoryRepository

class HistoryRepositoryImpl : HistoryRepository {
    private val _viewedMovies = MutableStateFlow<List<Movie>>(emptyList())
    override val viewedMovies: StateFlow<List<Movie>> = _viewedMovies.asStateFlow()

    override fun addViewedMovie(movie: Movie) {
        val current = _viewedMovies.value.toMutableList()
        current.removeAll { it.id == movie.id }
        current.add(0, movie) // Add to the beginning of the list
        if (current.size > 20) {
            current.removeAt(current.lastIndex)
        }
        _viewedMovies.value = current
    }
}
