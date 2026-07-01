package com.example.demomovieapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbResponse(
    val results: List<TmdbMovie>,
    @SerialName("total_pages") val totalPages: Int = 1
)
