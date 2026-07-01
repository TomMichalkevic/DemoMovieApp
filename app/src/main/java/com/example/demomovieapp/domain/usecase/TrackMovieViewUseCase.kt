package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.HistoryRepository
import javax.inject.Inject

class TrackMovieViewUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(movie: Movie) {
        repository.addViewedMovie(movie)
    }
}
