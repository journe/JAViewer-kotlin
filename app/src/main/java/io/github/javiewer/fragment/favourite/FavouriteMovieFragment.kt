package io.github.javiewer.fragment.favourite

import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import io.github.javiewer.JAViewer
import io.github.javiewer.adapter.ItemAdapter
import io.github.javiewer.adapter.MovieAdapter
import io.github.javiewer.view.decoration.MovieItemDecoration

/**
 * Project: JAViewer
 */
class FavouriteMovieFragment : FavouriteFragment() {
  override fun adapter(): ItemAdapter<*, *> {
    return object : MovieAdapter(JAViewer.CONFIGURATIONS.starredMovies, requireActivity()) {
      init {
        showIfHot = false
      }
    }
  }

  override fun decoration(): ItemDecoration {
    return MovieItemDecoration()
  }
}