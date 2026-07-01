package com.example.demomovieapp.domain.usecase

import com.example.demomovieapp.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTrailerUrlUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var classUnderTest: GetTrailerUrlUseCase

    @Before
    fun setUp() {
        repository = mockk()
        classUnderTest = GetTrailerUrlUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        val id = 123
        val expected = "https://youtube.com/watch?v=123"
        coEvery { repository.getTrailerUrl(id) } returns expected

        val result = classUnderTest(id)

        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.getTrailerUrl(id) }
    }
}
