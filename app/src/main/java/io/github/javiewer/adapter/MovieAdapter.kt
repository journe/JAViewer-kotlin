package io.github.javiewer.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import io.github.javiewer.R
import io.github.javiewer.activity.MovieActivity
import io.github.javiewer.adapter.MovieAdapter.ViewHolder
import io.github.javiewer.adapter.item.Movie

/**
 * Project: JAViewer
 */
open class MovieAdapter(
  private val movies: MutableList<Movie>,
  private val mParentActivity: Activity
) : ItemAdapter<Movie, ViewHolder>(movies) {
  protected var showIfHot = true
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val v = LayoutInflater.from(parent.context)
        .inflate(R.layout.card_movie, parent, false)
    return ViewHolder(v)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    val movie = movies[position]!!
    holder.parse(movie)
    holder.mCard!!.setOnClickListener {
      val intent = Intent(mParentActivity, MovieActivity::class.java)
      val bundle = Bundle()
//      bundle.putSerializable("movie", movie)
      intent.putExtras(bundle)
      mParentActivity.startActivity(intent)
    }
    holder.mImageCover!!.setImageDrawable(null)
    Glide.with(
        holder.mImageCover!!.context.applicationContext
    )
        .load(movie.coverUrl)
        .into(holder.mImageCover)
    holder.mImageHot!!.visibility = if (movie.hot && showIfHot) View.VISIBLE else View.GONE
  }

  class ViewHolder(view: View?) : RecyclerView.ViewHolder(
      view!!
  ) {
    var mTextTitle: TextView? = view!!.findViewById(R.id.movie_title)

    var mTextCode: TextView? = view!!.findViewById(R.id.movie_size)

    var mTextDate: TextView? = view!!.findViewById(R.id.movie_date)

    var mImageCover: ImageView? = view!!.findViewById(R.id.movie_cover)

    var mImageHot: ImageView? = view!!.findViewById(R.id.movie_hot)

    var mCard: CardView? = view!!.findViewById(R.id.card_movie)

    fun parse(movie: Movie) {
      mTextCode!!.text = movie.code
      mTextTitle!!.text = movie.title
      mTextDate!!.text = movie.date
    }

    init {
      ButterKnife.bind(this, view!!)
    }
  }
}