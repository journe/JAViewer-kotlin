package io.github.javiewer.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.robertlevonyan.views.chip.Chip
import io.github.javiewer.JAViewer
import io.github.javiewer.R
import io.github.javiewer.activity.DownloadActivity
import io.github.javiewer.activity.FavouriteActivity
import io.github.javiewer.base.BaseFragment
import io.github.javiewer.model.entity.Movie
import io.github.javiewer.model.entity.MovieDetail
import io.github.javiewer.model.network.provider.AVMOProvider
import io.github.javiewer.view.ViewUtil
import kotlinx.android.synthetic.main.activity_movie.fab
import kotlinx.android.synthetic.main.activity_movie.movie_content
import kotlinx.android.synthetic.main.activity_movie.movie_progress_bar
import kotlinx.android.synthetic.main.activity_movie.toolbar_layout_background
import kotlinx.android.synthetic.main.content_movie_genre.genre_flow_layout
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class MovieDetailFragment : BaseFragment() {
  var movie: Movie? = null
  private val args :MovieDetailFragmentArgs by navArgs()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_movie_detail, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    fab!!.setOnClickListener {
      val intent = Intent(context, DownloadActivity::class.java)
      val arguments = Bundle()
      arguments.putString("keyword", movie!!.code)
      intent.putExtras(arguments)
      startActivity(intent)
    }
    fab!!.bringToFront()
//    val call = JAViewer.SERVICE[args.link]
    val call = JAViewer.SERVICE.getMovieDetailS(args.link)
    call.enqueue(object : Callback<ResponseBody> {
      override fun onResponse(
        call: Call<ResponseBody>,
        response: Response<ResponseBody>
      ) {
        if (!response.isSuccessful) {
          return
        }
        try {
          val detail = AVMOProvider.parseMoviesDetail(
              response.body()!!
                  .string()
          )
//          detail.headers.add(0, Header.create("影片名", movie!!.title, null))
          displayInfo(detail,view)
          Glide.with(
              toolbar_layout_background!!.context.applicationContext
          )
              .load(detail.coverUrl)
              .into(toolbar_layout_background)
        } catch (e: IOException) {
          onFailure(call, e)
        }
      }

      override fun onFailure(
        call: Call<ResponseBody>,
        t: Throwable
      ) {
        t.printStackTrace()
      }
    })
  }

  private fun displayInfo(
    detail: MovieDetail,
    view: View
  ) {
    //Info
    run {
      val recycler_view: RecyclerView = view.findViewById<View>(R.id.headers_recycler_view) as RecyclerView
      val mIcon: ImageView = view.findViewById<View>(R.id.movie_icon_header) as ImageView
      if (detail.headers.isEmpty()) {
        val mText: TextView = view.findViewById<View>(R.id.header_empty_text) as TextView
        recycler_view.visibility = View.GONE
        mText.visibility = View.VISIBLE
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
//        recycler_view.adapter = MovieHeaderAdapter(detail.headers, this, mIcon)
        recycler_view.isNestedScrollingEnabled = false
      }
    }

    //Screenshots
    run {
      val recycler_view: RecyclerView =
        view.findViewById<View>(R.id.screenshots_recycler_view) as RecyclerView
      val mIcon: ImageView = view.findViewById<View>(R.id.movie_icon_screenshots) as ImageView
      if (detail.screenshots.isEmpty()) {
        val mText: TextView = view.findViewById<View>(R.id.screenshots_empty_text) as TextView
        recycler_view.visibility = View.GONE
        mText.visibility = View.VISIBLE
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
//        recycler_view.setAdapter(ScreenshotAdapter(detail.screenshots, this, mIcon, movie))
//        recycler_view.setLayoutManager(
//            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
//        )
        recycler_view.isNestedScrollingEnabled = false
      }
    }

    //Actress
    run {
      val recycler_view: RecyclerView =
        view.findViewById<View>(R.id.actresses_recycler_view) as RecyclerView
      val mIcon: ImageView = view.findViewById<View>(R.id.movie_icon_actresses) as ImageView
      if (detail.actresses.isEmpty()) {
        val mText: TextView = view.findViewById<View>(R.id.actresses_empty_text) as TextView
        recycler_view.visibility = View.GONE
        mText.visibility = View.VISIBLE
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
//        recycler_view.setAdapter(ActressPaletteAdapter(detail.actresses, this, mIcon))
//        recycler_view.setLayoutManager(
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        )
        recycler_view.isNestedScrollingEnabled = false
      }
    }

    //Genre
    run {
      val mIcon: ImageView = view.findViewById<View>(R.id.movie_icon_genre) as ImageView
      if (detail.genres.isEmpty()) {
        genre_flow_layout!!.visibility = View.GONE
        val mText: TextView = view.findViewById<View>(R.id.genre_empty_text) as TextView
        mText.visibility = View.VISIBLE
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
        for (i in detail.genres.indices) {
          val genre: MovieDetail.Genre = detail.genres.get(i)
          val view: View = layoutInflater.inflate(R.layout.chip_genre, genre_flow_layout, false)
          val chip: Chip = view.findViewById<View>(R.id.chip_genre) as Chip
          chip.setOnClickListener {
            if (genre.link != null) {
//              startActivity(
//                  MovieListActivity.newIntent(
//                      this@MovieDetailFragment, genre.name, genre.link
//                  )
//              )
            }
          }
          chip.chipText = genre.name
          genre_flow_layout!!.addView(view)
          if (i == 0) {
            ViewUtil.alignIconToView(mIcon, view)
          }
        }
      }
    }

    //Changing visibility
    movie_progress_bar!!.animate()
        .setDuration(200)
        .alpha(0f)
        .setListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            movie_progress_bar!!.visibility = View.GONE
          }
        })
        .start()

    //Slide Up Animation
    movie_content!!.visibility = View.VISIBLE
    movie_content!!.y = movie_content!!.y + 120
    movie_content!!.alpha = 0f
    movie_content!!.animate()
        .translationY(0f)
        .alpha(1f)
        .setDuration(500)
        .start()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
//        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(
    menu: Menu,
    menuInflater: MenuInflater
  ) {
    super.onCreateOptionsMenu(menu, menuInflater)

    menuInflater.inflate(R.menu.movie, menu)
    val mStarButton = menu.findItem(R.id.action_star)
    run {
      if (JAViewer.CONFIGURATIONS.starredMovies
              .contains(movie)
      ) {
        mStarButton!!.setIcon(R.drawable.ic_menu_star)
        mStarButton!!.title = "取消收藏"
      }
    }
    mStarButton.setOnMenuItemClickListener {
      if (JAViewer.CONFIGURATIONS.starredMovies.contains(movie)) {
        JAViewer.CONFIGURATIONS.starredMovies.remove(movie)
        mStarButton.setIcon(R.drawable.ic_menu_star_border)
        Snackbar.make((movie_content)!!, "已取消收藏", Snackbar.LENGTH_LONG)
            .show()
        mStarButton.setTitle("收藏")
      } else {
        val movies: MutableList<Movie?> = JAViewer.CONFIGURATIONS.starredMovies.toMutableList()
        movies.reverse()
        movies.add(movie)
        movies.reverse()
        mStarButton.setIcon(R.drawable.ic_menu_star)
        Snackbar.make((movie_content)!!, "已收藏", Snackbar.LENGTH_LONG)
            .show()
        mStarButton.title = "取消收藏"
      }
      JAViewer.CONFIGURATIONS.save()
      FavouriteActivity.update()
      true
    }
    val mShareButton = menu.findItem(R.id.action_share)
    mShareButton.setOnMenuItemClickListener(object : OnMenuItemClickListener {
      override fun onMenuItemClick(item: MenuItem): Boolean {
        try {
          val cache = File(activity!!.getExternalFilesDir("cache"), "screenshot")
//          //Generate screenshot
//          val os = FileOutputStream(cache)
//          val screenshot = screenBitmap
//          screenshot.compress(JPEG, 100, os)
//          os.flush()
//          os.close()
          val uri =
            FileProvider.getUriForFile(context!!, "io.github.javiewer.fileprovider", cache)
          // Uri uri = Uri.fromFile(cache);
          val intent = Intent(Intent.ACTION_SEND)
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
              .setType("image/jpeg")
              .putExtra(Intent.EXTRA_STREAM, uri)
          startActivity(Intent.createChooser(intent, "分享此影片"))
          return true
        } catch (e: Exception) {
          e.printStackTrace()
          Toast.makeText(context!!, "无法分享：" + e.message, Toast.LENGTH_SHORT)
              .show()
        }
        return false
      }
    })
  }
}