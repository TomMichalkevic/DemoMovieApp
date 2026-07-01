package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetViewedMoviesUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(): StateFlow<List<Movie>> {
        return repository.viewedMovies
    }
}
