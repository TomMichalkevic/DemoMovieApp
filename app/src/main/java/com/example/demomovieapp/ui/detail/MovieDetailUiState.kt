package com.example.demomovieapp.ui.detail

data class MovieDetailUiState(
    val trailerUrl: String? = null,
    val isLoadingTrailer: Boolean = false,
    val error: String? = null
)
