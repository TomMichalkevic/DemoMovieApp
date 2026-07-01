package com.example.demomovieapp.ui.main

import com.example.demomovieapp.domain.model.Movie

data class MainUiState(
    val popularMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val viewedMovies: List<Movie> = emptyList(),
    val searchResults: List<Movie>? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isFetchingMore: Boolean = false,
    val error: String? = null
)
