package com.example.demomovieapp.data.repository

import com.example.demomovieapp.data.remote.api.TmdbApi
import com.example.demomovieapp.data.remote.dto.TmdbMovie
import com.example.demomovieapp.data.remote.dto.TmdbResponse
import com.example.demomovieapp.data.remote.dto.TmdbVideo
import com.example.demomovieapp.data.remote.dto.TmdbVideoResponse
import com.example.demomovieapp.data.remote.dto.VideoType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {

    private lateinit var tmdbApi: TmdbApi
    private lateinit var classUnderTest: MovieRepositoryImpl

    private val movieDto = TmdbMovie(
        id = 1,
        title = "Test",
        overview = "Overview",
        posterPath = null,
        backdropPath = null,
        voteAverage = 5.0
    )

    private val movieResponse = TmdbResponse(
        results = listOf(movieDto),
        totalPages = 1
    )

    @Before
    fun setUp() {
        tmdbApi = mockk()
        classUnderTest = MovieRepositoryImpl(tmdbApi)
    }

    @Test
    fun `getPopular returns mapped movies`() = runTest {
        coEvery { tmdbApi.getPopular(any(), any()) } returns movieResponse

        val result = classUnderTest.getPopular()

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Test", result[0].title)
    }

    @Test
    fun `getTopRated returns mapped movies`() = runTest {
        coEvery { tmdbApi.getTopRated(any(), any()) } returns movieResponse

        val result = classUnderTest.getTopRated()

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
    }

    @Test
    fun `searchMovies returns mapped movies and total pages`() = runTest {
        coEvery { tmdbApi.searchMovies(any(), any(), any()) } returns movieResponse

        val result = classUnderTest.searchMovies("Test", 1)

        assertEquals(1, result.first.size)
        assertEquals(1, result.second)
    }

    @Test
    fun `getTrailerUrl returns first YouTube Trailer url`() = runTest {
        val videoResponse = TmdbVideoResponse(
            results = listOf(
                TmdbVideo(type = VideoType.TRAILER.value, key = "abc"),
                TmdbVideo(type = VideoType.TEASER.value, key = "def"),
                TmdbVideo(type = VideoType.TRAILER.value, key = "ghi")
            )
        )
        coEvery { tmdbApi.getVideos(any(), any()) } returns videoResponse

        val result = classUnderTest.getTrailerUrl(1)

        assertEquals(com.example.demomovieapp.R.raw.sample_video.toString(), result)
    }

    @Test
    fun `getTrailerUrl returns null if no Trailer found`() = runTest {
        val videoResponse = TmdbVideoResponse(
            results = listOf(
                TmdbVideo(type = VideoType.TEASER.value, key = "def")
            )
        )
        coEvery { tmdbApi.getVideos(any(), any()) } returns videoResponse

        val result = classUnderTest.getTrailerUrl(1)

        assertEquals(null, result)
    }
}
