package com.example.demomovieapp.data.remote.mapper

import com.example.demomovieapp.data.remote.dto.TmdbMovie
import com.example.demomovieapp.domain.model.Movie

fun TmdbMovie.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterUrl,
        backdropUrl = this.backdropUrl,
        voteAverage = this.voteAverage,
        trailerUrl = null
    )
}
