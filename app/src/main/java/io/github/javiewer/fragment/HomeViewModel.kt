package io.github.javiewer.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.javiewer.model.entity.Movie
import io.github.javiewer.model.repository.MovieListFactory

/**
 * Created by journey on 2020/11/29.
 */
class HomeViewModel : ViewModel() {
  fun postOfData(): LiveData<PagingData<Movie>> =
    MovieListFactory.repository()
        .fetchMovieList()
        .cachedIn(viewModelScope)
        .asLiveData()

}