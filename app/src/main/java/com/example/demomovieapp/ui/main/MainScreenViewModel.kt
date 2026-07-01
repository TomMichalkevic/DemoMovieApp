package com.example.demomovieapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.usecase.GetPopularMoviesUseCase
import com.example.demomovieapp.domain.usecase.GetTopRatedMoviesUseCase
import com.example.demomovieapp.domain.usecase.GetViewedMoviesUseCase
import com.example.demomovieapp.domain.usecase.SearchMoviesUseCase
import com.example.demomovieapp.domain.usecase.TrackMovieViewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val getTopRatedMovies: GetTopRatedMoviesUseCase,
    private val searchMovies: SearchMoviesUseCase,
    private val getViewedMovies: GetViewedMoviesUseCase,
    private val trackMovieViewUseCase: TrackMovieViewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var searchPage = 1
    private var maxSearchPages = 1
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            getViewedMovies().collect { viewedMovies ->
                _uiState.update { it.copy(viewedMovies = viewedMovies) }
            }
        }
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val popular = getPopularMovies()
                val topRated = getTopRatedMovies()
                _uiState.update { 
                    it.copy(
                        popularMovies = popular,
                        topRatedMovies = topRated,
                        isLoading = false
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "Unknown error occurred",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun search(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = null) }
            return
        }
        
        searchPage = 1
        maxSearchPages = 1

        searchJob = viewModelScope.launch {
            delay(500) // debounce
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val (movies, totalPages) = searchMovies(query, searchPage)
                maxSearchPages = totalPages
                _uiState.update { it.copy(searchResults = movies, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = "Failed to search movies: ${e.message}",
                        isLoading = false
                    ) 
                }
            }
        }
    }
    
    fun loadNextSearchPage() {
        if (_uiState.value.isLoading || _uiState.value.isFetchingMore || searchPage >= maxSearchPages) return
        val query = _uiState.value.searchQuery
        if (query.isBlank()) return

        searchPage++
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingMore = true, error = null) }
            try {
                val (newMovies, _) = searchMovies(query, searchPage)
                _uiState.update { 
                    it.copy(
                        searchResults = it.searchResults.orEmpty() + newMovies,
                        isFetchingMore = false
                    ) 
                }
            } catch (e: Exception) {
                searchPage--
                _uiState.update { 
                    it.copy(
                        error = "Failed to load more movies: ${e.message}",
                        isFetchingMore = false
                    ) 
                }
            }
        }
    }

    fun trackMovieView(movie: Movie) {
        trackMovieViewUseCase(movie)
    }
}
