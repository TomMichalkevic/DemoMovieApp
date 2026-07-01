package com.example.demomovieapp.ui.detail

import app.cash.turbine.test
import com.example.demomovieapp.domain.repository.MovieRepository
import com.example.demomovieapp.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: MovieRepository
    private lateinit var classUnderTest: MovieDetailViewModel

    @Before
    fun setUp() {
        repository = mockk()
        classUnderTest = MovieDetailViewModel(repository)
    }

    @Test
    fun `loadTrailer success updates state correctly`() = runTest {
        coEvery { repository.getTrailerUrl(1) } returns "https://youtube.com/watch?v=123"

        classUnderTest.trailerUrl.test {
            assertEquals(null, awaitItem())

            classUnderTest.loadTrailer(1)
            
            assertEquals("https://youtube.com/watch?v=123", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadTrailer error sets url to null`() = runTest {
        coEvery { repository.getTrailerUrl(1) } throws RuntimeException("Network Error")

        classUnderTest.trailerUrl.test {
            assertEquals(null, awaitItem()) // Initial state

            classUnderTest.loadTrailer(1)
            
            // It remains null or is set to null, since the exception sets it to null
            // We just expect another emission of null or no emission if distinctUntilChanged (StateFlow)
            // StateFlow conflates duplicate consecutive values, so there might be no second emission.
            cancelAndIgnoreRemainingEvents()
        }
        
        assertEquals(null, classUnderTest.trailerUrl.value)
    }
}
