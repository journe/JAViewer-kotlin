package io.github.javiewer.fragment.actress

/**
 * Created by journey on 2020/12/18.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.javiewer.R
import io.github.javiewer.model.entity.MovieDetail.Actress
import io.github.javiewer.util.imageEngine.ImageLoader
import kotlinx.android.synthetic.main.layout_actress.view.actress_img
import kotlinx.android.synthetic.main.layout_actress.view.actress_name

class ActressItemPagingAdapter :
    PagingDataAdapter<Actress, ActressItemPagingAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.layout_actress, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    val item = getItem(position)!!
    holder.bind(item)

    with(holder.mView) {
      tag = item
    }
  }

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    fun bind(item: Actress) {
      title.text = item.name
      ImageLoader.loadImage(image, item.imageUrl)
    }

    val title: TextView = mView.actress_name
    val image: ImageView = mView.actress_img
  }

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Actress>() {
      override fun areItemsTheSame(
        oldItem: Actress,
        newItem: Actress
      ): Boolean {
        return oldItem.name == newItem.name
      }

      override fun areContentsTheSame(
        oldItem: Actress,
        newItem: Actress
      ): Boolean {
        return oldItem.name == newItem.name
      }
    }
  }
}