package com.example.demomovieapp.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.demomovieapp.data.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: Movie,
    onPlayTrailer: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = viewModel()
) {
    val trailerUrl by viewModel.trailerUrl.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingTrailer.collectAsStateWithLifecycle()
    
    var isPosterVisible by remember { mutableStateOf(false) }

    LaunchedEffect(movie.id) {
        viewModel.loadTrailer(movie.id)
        isPosterVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Backdrop image section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                ) {
                    SubcomposeAsyncImage(
                        model = movie.backdropUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    )

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else if (trailerUrl != null) {
                        FilledIconButton(
                            onClick = { onPlayTrailer(trailerUrl!!) },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Play Trailer",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                // Overlaid Poster with Animation
                AnimatedVisibility(
                    visible = isPosterVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(500)
                    ) + fadeIn(),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp)
                        .offset(y = 48.dp) // Hang over the edge of the backdrop
                ) {
                    Card(
                        modifier = Modifier
                            .width(120.dp)
                            .aspectRatio(0.66f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = movie.posterUrl,
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp)) // Spacer to account for the poster overlap

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Rating: ${movie.voteAverage}/10",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
