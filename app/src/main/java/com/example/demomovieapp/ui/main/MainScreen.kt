package com.example.demomovieapp.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.demomovieapp.domain.model.Movie

@Composable
fun MainScreen(
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val handleMovieClick: (Movie) -> Unit = { movie ->
        viewModel.trackMovieView(movie)
        onMovieClick(movie)
    }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::search,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text(stringResource(com.example.demomovieapp.R.string.search_movies)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(com.example.demomovieapp.R.string.search_content_desc)) },
            singleLine = true,
            shape = MaterialTheme.shapes.extraLarge
        )

        if (uiState.isLoading && uiState.popularMovies.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null && uiState.popularMovies.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (uiState.searchResults != null) {
                    item { SectionTitle(stringResource(com.example.demomovieapp.R.string.search_results)) }
                    if (uiState.searchResults!!.isEmpty() && !uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(com.example.demomovieapp.R.string.no_movies_found, uiState.searchQuery),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        itemsIndexed(uiState.searchResults!!) { index, movie ->
                            MovieListItem(movie = movie, onClick = { handleMovieClick(movie) })
                            
                            // Trigger load next page when we reach the end
                            if (index == uiState.searchResults!!.lastIndex) {
                                androidx.compose.runtime.LaunchedEffect(index) {
                                    viewModel.loadNextSearchPage()
                                }
                            }
                        }
                        
                        if (uiState.isFetchingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                } else {
                    if (uiState.viewedMovies.isNotEmpty()) {
                        item {
                            SectionTitle(stringResource(com.example.demomovieapp.R.string.viewed_movies))
                            MovieHorizontalList(uiState.viewedMovies, handleMovieClick)
                        }
                    }
                    if (uiState.popularMovies.isNotEmpty()) {
                        item {
                            SectionTitle(stringResource(com.example.demomovieapp.R.string.popular))
                            MovieHorizontalList(uiState.popularMovies, handleMovieClick)
                        }
                    }
                    if (uiState.topRatedMovies.isNotEmpty()) {
                        item {
                            SectionTitle(stringResource(com.example.demomovieapp.R.string.top_rated))
                            MovieHorizontalList(uiState.topRatedMovies, handleMovieClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun MovieHorizontalList(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie = movie, modifier = Modifier.width(120.dp), onClick = { onMovieClick(movie) })
        }
    }
}

@Composable
fun MovieListItem(movie: Movie, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(modifier = Modifier.width(60.dp).aspectRatio(0.6f)) {
            SubcomposeAsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun MovieCard(movie: Movie, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .aspectRatio(0.6f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        SubcomposeAsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        )
    }
}
