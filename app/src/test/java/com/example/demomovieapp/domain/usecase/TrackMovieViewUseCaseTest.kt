package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.HistoryRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TrackMovieViewUseCaseTest {

    private lateinit var repository: HistoryRepository
    private lateinit var classUnderTest: TrackMovieViewUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        classUnderTest = TrackMovieViewUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() {
        val movie = Movie(1, "Title", "Overview", "", "", 1.0)
        
        classUnderTest(movie)

        verify(exactly = 1) { repository.addViewedMovie(movie) }
    }
}
