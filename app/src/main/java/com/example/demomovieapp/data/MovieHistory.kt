package com.example.demomovieapp.data

import com.example.demomovieapp.domain.model.Movie

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MovieHistory {
    private val _viewedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val viewedMovies: StateFlow<List<Movie>> = _viewedMovies.asStateFlow()

    fun addViewedMovie(movie: Movie) {
        val current = _viewedMovies.value.toMutableList()
        current.removeAll { it.id == movie.id }
        current.add(0, movie)
        _viewedMovies.value = current
    }
}
