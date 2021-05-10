package io.github.javiewer.util.imageEngine

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE
import io.github.javiewer.R.drawable
import io.github.javiewer.view.SquareTopCrop

/**
 * Created by journey on 2020/4/27.
 */
object ImageLoader {
  fun loadImage(
    imageView: ImageView,
    imageUrl: String?
  ) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .placeholder(drawable.ic_movie_actresses)
        .diskCacheStrategy(SOURCE) // override default RESULT cache and apply transform always
        .skipMemoryCache(true) // do not reuse the transformed result while running
        .transform(SquareTopCrop(imageView!!.context))
        .dontAnimate()
        .into(imageView)
  }
}
