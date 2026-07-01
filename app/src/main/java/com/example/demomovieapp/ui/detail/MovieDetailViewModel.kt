package com.example.demomovieapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demomovieapp.domain.usecase.GetTrailerUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getTrailerUrl: GetTrailerUrlUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadTrailer(movieId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingTrailer = true, error = null) }
            try {
                val url = getTrailerUrl(movieId)
                _uiState.update { it.copy(trailerUrl = url, isLoadingTrailer = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        trailerUrl = null, 
                        error = e.message ?: "Failed to load trailer",
                        isLoadingTrailer = false
                    ) 
                }
            }
        }
    }
}
