package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetTrailerUrlUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): String? {
        return repository.getTrailerUrl(movieId)
    }
}
