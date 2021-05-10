package io.github.javiewer.fragment.favourite

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import io.github.javiewer.JAViewer
import io.github.javiewer.adapter.ActressAdapter
import io.github.javiewer.adapter.ItemAdapter
import io.github.javiewer.view.decoration.ActressItemDecoration

/**
 * Project: JAViewer
 */
class FavouriteActressFragment : FavouriteFragment() {
  override fun adapter(): ItemAdapter<*, *> {
    return ActressAdapter(JAViewer.CONFIGURATIONS!!.starredActresses, activity as Activity)
  }

  override fun decoration(): ItemDecoration? {
    return ActressItemDecoration()
  }
}