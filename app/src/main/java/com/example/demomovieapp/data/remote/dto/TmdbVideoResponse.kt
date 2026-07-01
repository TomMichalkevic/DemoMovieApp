package com.example.demomovieapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TmdbVideoResponse(
    val results: List<TmdbVideo>
)
