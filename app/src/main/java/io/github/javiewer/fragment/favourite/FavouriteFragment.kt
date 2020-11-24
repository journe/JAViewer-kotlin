package io.github.javiewer.fragment.favourite

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import io.github.javiewer.adapter.ItemAdapter
import io.github.javiewer.adapter.item.Movie
import io.github.javiewer.fragment.RecyclerFragment
import kotlinx.android.synthetic.main.fragment_recycler.recycler_view
import kotlinx.android.synthetic.main.fragment_recycler.refresh_layout

/**
 * Project: JAViewer
 */
abstract class FavouriteFragment : RecyclerFragment<Movie?, LinearLayoutManager?>() {
  fun update() {
    val adapter = adapter
    adapter?.notifyDataSetChanged()
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    setLayoutManager(LinearLayoutManager(this.context))
    adapter = adapter()
    //this.setAdapter(adapter =
    refresh_layout.isEnabled = false
    if (decoration() != null) {
      recycler_view.addItemDecoration(decoration()!!)
    }
    super.onActivityCreated(savedInstanceState)
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  abstract fun adapter(): ItemAdapter<*, *>?
  open fun decoration(): ItemDecoration? {
    return null
  }
}