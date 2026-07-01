package com.example.demomovieapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demomovieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _trailerUrl = MutableStateFlow<String?>(null)
    val trailerUrl: StateFlow<String?> = _trailerUrl.asStateFlow()

    private val _isLoadingTrailer = MutableStateFlow(false)
    val isLoadingTrailer: StateFlow<Boolean> = _isLoadingTrailer.asStateFlow()

    fun loadTrailer(movieId: Int) {
        viewModelScope.launch {
            _isLoadingTrailer.value = true
            try {
                val url = repository.getTrailerUrl(movieId)
                _trailerUrl.value = url
            } catch (e: Exception) {
                _trailerUrl.value = null
            } finally {
                _isLoadingTrailer.value = false
            }
        }
    }
}
