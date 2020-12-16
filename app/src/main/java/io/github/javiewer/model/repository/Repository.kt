package io.github.javiewer.model.repository

import androidx.paging.PagingData
import io.github.javiewer.model.entity.Movie
import kotlinx.coroutines.flow.Flow

interface Repository {
  fun fetchMovieList(): Flow<PagingData<Movie>>

  suspend fun fetchPokemonInfo(name: String): Flow<JavResult<Movie>>

//  suspend fun fetchPokemonByParameter(parameter: String): Flow<PagingData<Movie>>
}