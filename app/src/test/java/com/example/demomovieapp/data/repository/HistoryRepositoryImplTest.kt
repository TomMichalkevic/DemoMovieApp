package com.example.demomovieapp.data.repository

import com.example.demomovieapp.domain.model.Movie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HistoryRepositoryImplTest {

    private lateinit var classUnderTest: HistoryRepositoryImpl

    private val movie1 = Movie(1, "Movie 1", "Overview", "", "", 1.0)
    private val movie2 = Movie(2, "Movie 2", "Overview", "", "", 2.0)
    private val movie3 = Movie(3, "Movie 3", "Overview", "", "", 3.0)

    @Before
    fun setUp() {
        classUnderTest = HistoryRepositoryImpl()
    }

    @Test
    fun `addViewedMovie adds movie to the top of the list`() = runTest {
        classUnderTest.addViewedMovie(movie1)
        
        val history = classUnderTest.viewedMovies.first()
        assertEquals(1, history.size)
        assertEquals(movie1, history[0])
    }

    @Test
    fun `addViewedMovie removes older duplicate and adds to top`() = runTest {
        classUnderTest.addViewedMovie(movie1)
        classUnderTest.addViewedMovie(movie2)
        classUnderTest.addViewedMovie(movie1)
        
        val history = classUnderTest.viewedMovies.first()
        assertEquals(2, history.size)
        assertEquals(movie1, history[0])
        assertEquals(movie2, history[1])
    }

    @Test
    fun `addViewedMovie keeps maximum of 20 movies`() = runTest {
        for (i in 1..25) {
            classUnderTest.addViewedMovie(Movie(i, "Movie $i", "Overview", "", "", 0.0))
        }
        
        val history = classUnderTest.viewedMovies.first()
        assertEquals(20, history.size)
        assertEquals(25, history[0].id) // Most recent
        assertEquals(6, history[19].id) // Oldest kept
    }
}
