package com.example.demomovieapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.demomovieapp.ui.main.MainScreen
import com.example.demomovieapp.ui.detail.MovieDetailScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Main)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Main> {
          MainScreen(
              onMovieClick = { movie -> backStack.add(Detail(movie)) }, 
              modifier = Modifier.safeDrawingPadding().padding(16.dp)
          )
        }
        entry<Detail> { detailKey ->
          MovieDetailScreen(
              movie = detailKey.movie,
              onPlayTrailer = { url -> backStack.add(VideoPlayer(url)) },
              onBackClick = { backStack.removeLastOrNull() },
              modifier = Modifier.safeDrawingPadding()
          )
        }
        entry<VideoPlayer> { videoKey ->
            com.example.demomovieapp.ui.player.VideoPlayerScreen(videoUrl = videoKey.videoUrl)
        }
      },
  )
}
