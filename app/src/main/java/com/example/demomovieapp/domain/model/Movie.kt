package com.example.demomovieapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val voteAverage: Double,
    val trailerUrl: String? = null
)
