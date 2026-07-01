package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return repository.getPopular()
    }
}
