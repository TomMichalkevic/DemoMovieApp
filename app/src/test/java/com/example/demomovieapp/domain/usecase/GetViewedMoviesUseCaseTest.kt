package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.HistoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetViewedMoviesUseCaseTest {

    private lateinit var repository: HistoryRepository
    private lateinit var classUnderTest: GetViewedMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        classUnderTest = GetViewedMoviesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() {
        val flow = MutableStateFlow(listOf(Movie(1, "Title", "Overview", "", "", 1.0)))
        every { repository.viewedMovies } returns flow

        val result = classUnderTest()

        assertEquals(flow, result)
        verify(exactly = 1) { repository.viewedMovies }
    }
}
