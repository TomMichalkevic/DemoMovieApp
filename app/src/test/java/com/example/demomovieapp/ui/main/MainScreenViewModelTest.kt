package com.example.demomovieapp.ui.main

import app.cash.turbine.test
import com.example.demomovieapp.domain.model.Movie
import com.example.demomovieapp.domain.repository.HistoryRepository
import com.example.demomovieapp.domain.repository.MovieRepository
import com.example.demomovieapp.domain.usecase.GetPopularMoviesUseCase
import com.example.demomovieapp.domain.usecase.GetTopRatedMoviesUseCase
import com.example.demomovieapp.domain.usecase.GetViewedMoviesUseCase
import com.example.demomovieapp.domain.usecase.SearchMoviesUseCase
import com.example.demomovieapp.domain.usecase.TrackMovieViewUseCase
import com.example.demomovieapp.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
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

    private lateinit var getPopularMovies: GetPopularMoviesUseCase
    private lateinit var getTopRatedMovies: GetTopRatedMoviesUseCase
    private lateinit var searchMovies: SearchMoviesUseCase
    private lateinit var getViewedMovies: GetViewedMoviesUseCase
    private lateinit var trackMovieViewUseCase: TrackMovieViewUseCase
    
    private lateinit var classUnderTest: MainScreenViewModel

    private val movie1 = Movie(1, "Movie 1", "Overview", "", "", 1.0)
    private val movie2 = Movie(2, "Movie 2", "Overview", "", "", 2.0)

    @Before
    fun setUp() {
        getPopularMovies = mockk()
        getTopRatedMovies = mockk()
        searchMovies = mockk()
        getViewedMovies = mockk()
        trackMovieViewUseCase = mockk(relaxed = true)

        coEvery { getPopularMovies() } returns listOf(movie1)
        coEvery { getTopRatedMovies() } returns listOf(movie2)
        every { getViewedMovies() } returns MutableStateFlow(emptyList())

        classUnderTest = MainScreenViewModel(
            getPopularMovies,
            getTopRatedMovies,
            searchMovies,
            getViewedMovies,
            trackMovieViewUseCase
        )
    }

    @Test
    fun `initialization loads popular and top rated movies`() = runTest {
        classUnderTest.uiState.test {
            val initialState = awaitItem()
            if (initialState.isLoading) {
                val loadedState = awaitItem()
                assertEquals(listOf(movie1), loadedState.popularMovies)
                assertEquals(listOf(movie2), loadedState.topRatedMovies)
            } else {
                assertEquals(listOf(movie1), initialState.popularMovies)
                assertEquals(listOf(movie2), initialState.topRatedMovies)
            }
        }
    }

    @Test
    fun `search with query fetches movies with debounce`() = runTest {
        coEvery { searchMovies("test", 1) } returns Pair(listOf(movie1, movie2), 1)

        classUnderTest.uiState.test {
            // Initial state from init {} could be loading or loaded, but searchResults is null
            assertEquals(null, awaitItem().searchResults)
            
            // Perform action
            classUnderTest.search("test")

            // State changes to searchQuery = "test"
            val stateAfterSearch = awaitItem()
            assertEquals("test", stateAfterSearch.searchQuery)

            // Advance time to pass the 500ms debounce
            advanceTimeBy(501)
            
            // Wait for loading to finish and result to arrive
            val resultState = awaitItem()
            if (resultState.isLoading) {
                assertEquals(listOf(movie1, movie2), awaitItem().searchResults)
            } else {
                assertEquals(listOf(movie1, movie2), resultState.searchResults)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search with blank query clears results`() = runTest {
        classUnderTest.uiState.test {
            // Initial state
            awaitItem()
            
            // Set some initial search results first
            classUnderTest.search("test")
            val stateWithQuery = awaitItem()
            
            advanceTimeBy(501)
            // Skip loading
            val loadedState = awaitItem()
            if (loadedState.isLoading) {
                awaitItem() // the result state
            }
            
            // Perform action with blank query
            classUnderTest.search("")

            // Next state will update searchQuery to "" and searchResults to null
            val clearedState = awaitItem()
            assertEquals("", clearedState.searchQuery)
            if (clearedState.searchResults != null) {
                val finalState = awaitItem()
                assertEquals(null, finalState.searchResults)
            }
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `trackMovieView delegates to use case`() {
        classUnderTest.trackMovieView(movie1)

        verify { trackMovieViewUseCase(movie1) }
    }
}
