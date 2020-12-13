package io.github.javiewer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.javiewer.R
import io.github.javiewer.model.entity.Movie

class MovieListAdapter :
    PagingDataAdapter<Movie, MovieListAdapter.ViewHolder>(diffCallback) {

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
    if (getItem(position) == null) {
      return
    }
    val movie = getItem(position)!!
    holder.parse(movie)
    holder.mCard!!.setOnClickListener {
      it.findNavController().navigate(R.id.nav_movie_detail)
//            val intent = Intent(context, MovieActivity::class.java)
//            val bundle = Bundle()
//      bundle.putSerializable("movie", movie)
//            intent.putExtras(bundle)
//            mParentActivity.startActivity(intent)
    }
    holder.mImageCover!!.setImageDrawable(null)
    Glide.with(
        holder.mImageCover!!.context.applicationContext
    )
        .load(movie.coverUrl)
        .into(holder.mImageCover)
    holder.mImageHot!!.visibility = if (movie.hot) View.VISIBLE else View.GONE
  }

  inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(
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
  }

  companion object {
    val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
      override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
      ): Boolean =
        oldItem.code == newItem.code

      override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
      ): Boolean =
        oldItem == newItem
    }
  }
}
