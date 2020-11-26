package io.github.javiewer.adapter

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Project: JAViewer
 */
abstract class ItemAdapter<I, VH : ViewHolder?>(private val items: MutableList<I>) : Adapter<VH>() {
  fun getItems(): MutableList<I> {
    return items
  }

  fun setItems(items: List<I>) {
    val size = getItems().size
    if (size > 0) {
      getItems().clear()
      notifyItemRangeRemoved(0, size)
    }
    getItems().addAll(items)
    notifyItemRangeInserted(0, items.size)
  }

  override fun getItemCount(): Int {
    return getItems().size
  }

  override fun onViewDetachedFromWindow(holder: VH) {
    holder!!.itemView.clearAnimation()
    super.onViewDetachedFromWindow(holder)
  }
}