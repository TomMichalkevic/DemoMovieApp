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

class SearchMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var classUnderTest: SearchMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        classUnderTest = SearchMoviesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        val query = "Batman"
        val page = 2
        val expected = Pair(listOf(Movie(1, "Batman", "Overview", "", "", 1.0)), 5)
        coEvery { repository.searchMovies(query, page) } returns expected

        val result = classUnderTest(query, page)

        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.searchMovies(query, page) }
    }
}
