package io.github.javiewer.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE
import io.github.javiewer.R.drawable
import io.github.javiewer.R.id
import io.github.javiewer.R.layout
import io.github.javiewer.adapter.ActressAdapter.ViewHolder
import io.github.javiewer.model.entity.MovieDetail
import io.github.javiewer.view.SquareTopCrop
import io.github.javiewer.view.listener.ActressClickListener
import io.github.javiewer.view.listener.ActressLongClickListener
import java.util.ArrayList

/**
 * Project: JAViewer
 */
class ActressAdapter(
  actresses: ArrayList<MovieDetail.Actress>,
  private val mParentActivity: Activity
) : ItemAdapter<MovieDetail.Actress, ViewHolder?>(actresses) {
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val v = LayoutInflater.from(parent.context)
        .inflate(layout.layout_actress, parent, false)
    return ViewHolder(v)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    val actress = getItems()[position]!!
    holder.parse(actress)
    holder.mLayout!!.setOnClickListener(ActressClickListener(actress, mParentActivity))
    holder.mLayout!!.setOnLongClickListener(ActressLongClickListener(actress, mParentActivity))
    holder.mImage!!.setImageDrawable(null)
    Glide.with(
        holder.mImage!!.context.applicationContext
    )
        .load(actress.imageUrl)
        .placeholder(drawable.ic_movie_actresses)
        .diskCacheStrategy(SOURCE) // override default RESULT cache and apply transform always
        .skipMemoryCache(true) // do not reuse the transformed result while running
        .transform(SquareTopCrop(holder.mImage!!.context))
        .dontAnimate()
        .into(holder.mImage)
  }

  class ViewHolder(view: View?) : RecyclerView.ViewHolder(
      view!!
  ) {
    @BindView(id.actress_name)
    var mTextName: TextView? = null

    @BindView(id.actress_img)
    var mImage: ImageView? = null

    @BindView(id.layout_actress)
    var mLayout: View? = null
    fun parse(actress: MovieDetail.Actress) {
      mTextName!!.text = actress.name
      mTextName!!.isSelected = true
    }

    init {
      ButterKnife.bind(this, view!!)
    }
  }
}