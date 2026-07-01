package com.example.demomovieapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbResponse(
    val results: List<TmdbMovie>,
    @SerialName("total_pages") val totalPages: Int = 1
)

@Serializable
data class TmdbMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0
) {
    val posterUrl: String get() = "https://image.tmdb.org/t/p/w500$posterPath"
    val backdropUrl: String get() = "https://image.tmdb.org/t/p/w780$backdropPath"
}

@Serializable
data class TmdbVideoResponse(
    val results: List<TmdbVideo>
)

@Serializable
data class TmdbVideo(
    val type: String,
    val key: String
)

// Domain model
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
