package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPopularMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var classUnderTest: GetPopularMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        classUnderTest = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        val movies = listOf(Movie(1, "Title", "Overview", "", "", 1.0))
        coEvery { repository.getPopular() } returns movies

        val result = classUnderTest()

        assertEquals(movies, result)
        coVerify(exactly = 1) { repository.getPopular() }
    }
}
