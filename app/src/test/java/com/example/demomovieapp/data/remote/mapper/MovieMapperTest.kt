package com.example.demomovieapp.data.remote.mapper

import com.example.demomovieapp.data.remote.dto.TmdbMovie
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieMapperTest {

    @Test
    fun `toDomainModel maps correctly when all fields are present`() {
        val dto = TmdbMovie(
            id = 1,
            title = "Test Movie",
            overview = "Test Overview",
            posterPath = "/path.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.5
        )

        val domain = dto.toDomain()

        assertEquals(1, domain.id)
        assertEquals("Test Movie", domain.title)
        assertEquals("Test Overview", domain.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/path.jpg", domain.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", domain.backdropUrl)
        assertEquals(8.5, domain.voteAverage, 0.0)
    }

    @Test
    fun `toDomainModel handles null fields appropriately`() {
        val dto = TmdbMovie(
            id = 2,
            title = "",
            overview = "",
            posterPath = null,
            backdropPath = null,
            voteAverage = 0.0
        )

        val domain = dto.toDomain()

        assertEquals(2, domain.id)
        assertEquals("", domain.title)
        assertEquals("", domain.overview)
        assertEquals("https://image.tmdb.org/t/p/w500null", domain.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780null", domain.backdropUrl)
        assertEquals(0.0, domain.voteAverage, 0.0)
    }
}
