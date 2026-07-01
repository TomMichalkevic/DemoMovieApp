package com.example.demomovieapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demomovieapp.data.Movie
import com.example.demomovieapp.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface MainScreenUiState {
    object Loading : MainScreenUiState
    data class Error(val message: String) : MainScreenUiState
    data class Success(val movies: List<Movie>) : MainScreenUiState
}

class MainScreenViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Loading)
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = MainScreenUiState.Loading
            try {
                val movies = repository.getNowPlaying()
                _uiState.value = MainScreenUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = MainScreenUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
