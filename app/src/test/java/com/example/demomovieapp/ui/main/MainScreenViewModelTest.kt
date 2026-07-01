package com.example.demomovieapp.ui.main

import app.cash.turbine.test
import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.HistoryRepository
import com.example.demomovieapp.domain.repository.MovieRepository
import com.example.demomovieapp.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieRepository: MovieRepository
    private lateinit var historyRepository: HistoryRepository
    private lateinit var classUnderTest: MainScreenViewModel

    private val movie1 = Movie(1, "Movie 1", "Overview", "", "", 1.0)
    private val movie2 = Movie(2, "Movie 2", "Overview", "", "", 2.0)

    @Before
    fun setUp() {
        movieRepository = mockk()
        historyRepository = mockk(relaxed = true)

        coEvery { movieRepository.getPopular() } returns listOf(movie1)
        coEvery { movieRepository.getTopRated() } returns listOf(movie2)
        every { historyRepository.viewedMovies } returns MutableStateFlow(emptyList())

        classUnderTest = MainScreenViewModel(movieRepository, historyRepository)
    }

    @Test
    fun `initialization loads popular and top rated movies`() = runTest {
        classUnderTest.popularMovies.test {
            assertEquals(listOf(movie1), awaitItem())
        }

        classUnderTest.topRatedMovies.test {
            assertEquals(listOf(movie2), awaitItem())
        }
    }

    @Test
    fun `search with query fetches movies with debounce`() = runTest {
        coEvery { movieRepository.searchMovies("test", 1) } returns Pair(listOf(movie1, movie2), 1)

        classUnderTest.search("test")

        classUnderTest.searchResults.test {
            assertEquals(null, awaitItem()) // Initial
            
            // Advance time to pass the 500ms debounce
            advanceTimeBy(501)
            
            assertEquals(listOf(movie1, movie2), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search with blank query clears results`() = runTest {
        classUnderTest.search("")

        classUnderTest.searchResults.test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `trackMovieView delegates to history repository`() {
        classUnderTest.trackMovieView(movie1)

        verify { historyRepository.addViewedMovie(movie1) }
    }
}
