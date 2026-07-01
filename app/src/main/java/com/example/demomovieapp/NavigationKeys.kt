package com.example.demomovieapp

import androidx.navigation3.runtime.NavKey
import com.example.demomovieapp.data.Movie
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data class Detail(val movie: Movie) : NavKey
@Serializable data class VideoPlayer(val videoUrl: String) : NavKey
