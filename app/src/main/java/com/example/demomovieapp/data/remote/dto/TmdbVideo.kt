package com.example.demomovieapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TmdbVideo(
    val type: String,
    val key: String
)
