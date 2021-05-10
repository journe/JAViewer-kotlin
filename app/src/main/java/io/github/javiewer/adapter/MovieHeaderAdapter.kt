package io.github.javiewer.adapter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import butterknife.BindView
import butterknife.ButterKnife
import io.github.javiewer.R.color
import io.github.javiewer.R.id
import io.github.javiewer.R.layout
import io.github.javiewer.activity.MovieListActivity
import io.github.javiewer.adapter.MovieHeaderAdapter.ViewHolder
import io.github.javiewer.model.entity.MovieDetail.Header
import io.github.javiewer.view.ViewUtil

/**
 * Project: JAViewer
 */
class MovieHeaderAdapter(
  private val headers: List<Header>?,
  private val mParentActivity: Activity,
  private val mIcon: ImageView
) : Adapter<ViewHolder>() {
  var first = true
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val v = LayoutInflater.from(parent.context)
        .inflate(layout.layout_header, parent, false)
    return ViewHolder(v)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    val (name, value, link) = headers!![position]
    if (name != null && value != null) {
      holder.itemView.setOnLongClickListener {
        val clip = mParentActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clip.primaryClip = ClipData.newPlainText(name, value)
        Toast.makeText(mParentActivity, "已复制到剪贴板", Toast.LENGTH_SHORT)
            .show()
        true
      }
      holder.mHeaderName!!.text = name
      holder.mHeaderValue!!.text = value
      if (link != null) {
        holder.mHeaderValue!!.paintFlags =
          holder.mHeaderValue!!.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        holder.mHeaderValue!!.setTextColor(
            ResourcesCompat.getColor(mParentActivity.resources, color.colorAccent, null)
        )
        holder.mHeaderValue!!.setOnClickListener {
          val intent = Intent(mParentActivity, MovieListActivity::class.java)
          val bundle = Bundle()
          bundle.putString("title", "$name $value")
          bundle.putString("link", link)
          intent.putExtras(bundle)
          mParentActivity.startActivity(intent)
        }
      }
      if (first) {
        ViewUtil.alignIconToView(mIcon, holder.mHeaderName)
        first = false
      }
    }
  }

  override fun getItemCount(): Int {
    return headers?.size ?: 0
  }

  inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(
      view!!
  ) {
    @BindView(id.header_name)
    var mHeaderName: TextView? = null

    @BindView(id.header_value)
    var mHeaderValue: TextView? = null

    init {
      ButterKnife.bind(this, view!!)
    }
  }
}