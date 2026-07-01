package com.example.demomovieapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demomovieapp.data.Movie
import com.example.demomovieapp.data.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val topRatedMovies: StateFlow<List<Movie>> = _topRatedMovies.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Movie>?>(null)
    val searchResults: StateFlow<List<Movie>?> = _searchResults.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _popularMovies.value = repository.getPopular()
                _topRatedMovies.value = repository.getTopRated()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        if (query.isBlank()) {
            _searchResults.value = null
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // debounce
            _isLoading.value = true
            try {
                _searchResults.value = repository.searchMovies(query)
            } catch (e: Exception) {
                // Ignore search errors for simplicity
            } finally {
                _isLoading.value = false
            }
        }
    }

    val viewedMovies: StateFlow<List<Movie>> = com.example.demomovieapp.data.MovieHistory.viewedMovies

    fun trackMovieView(movie: Movie) {
        com.example.demomovieapp.data.MovieHistory.addViewedMovie(movie)
    }
}
