package io.github.javiewer.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.javiewer.adapter.MovieListAdapter
import io.github.javiewer.model.entity.Movie
import io.github.javiewer.model.repository.Repository

/**
 * Created by journey on 2020/11/29.
 */
class AllViewModel(private val repository: Repository) : ViewModel() {
  val movieList by lazy {
    repository.fetchMovieList()
        .cachedIn(viewModelScope)
        .asLiveData()
  }

//  fun postOfData(): LiveData<PagingData<Movie>> =
//    repository.fetchMovieList()
//        .cachedIn(viewModelScope)
//        .asLiveData()

}