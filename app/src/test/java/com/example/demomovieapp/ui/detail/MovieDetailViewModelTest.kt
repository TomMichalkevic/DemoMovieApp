package com.example.demomovieapp.ui.detail

import app.cash.turbine.test
import com.example.demomovieapp.domain.usecase.GetTrailerUrlUseCase
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

    private lateinit var getTrailerUrl: GetTrailerUrlUseCase
    private lateinit var classUnderTest: MovieDetailViewModel

    @Before
    fun setUp() {
        getTrailerUrl = mockk()
        classUnderTest = MovieDetailViewModel(getTrailerUrl)
    }

    @Test
    fun `loadTrailer success updates state correctly`() = runTest {
        coEvery { getTrailerUrl(1) } returns "https://youtube.com/watch?v=123"

        classUnderTest.uiState.test {
            assertEquals(null, awaitItem().trailerUrl)

            classUnderTest.loadTrailer(1)
            
            val loadingState = awaitItem()
            if (loadingState.isLoadingTrailer) {
                assertEquals("https://youtube.com/watch?v=123", awaitItem().trailerUrl)
            } else {
                assertEquals("https://youtube.com/watch?v=123", loadingState.trailerUrl)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadTrailer error sets url to null`() = runTest {
        coEvery { getTrailerUrl(1) } throws RuntimeException("Network Error")

        classUnderTest.uiState.test {
            assertEquals(null, awaitItem().trailerUrl) // Initial state

            classUnderTest.loadTrailer(1)
            
            val loadingState = awaitItem()
            if (loadingState.isLoadingTrailer) {
                assertEquals(null, awaitItem().trailerUrl)
            } else {
                assertEquals(null, loadingState.trailerUrl)
            }
            cancelAndIgnoreRemainingEvents()
        }
        
        assertEquals(null, classUnderTest.uiState.value.trailerUrl)
    }
}
