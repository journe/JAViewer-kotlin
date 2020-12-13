package io.github.javiewer.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.Bitmap.CompressFormat.JPEG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import butterknife.BindView
import butterknife.ButterKnife
import cn.jzvd.JZVideoPlayer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nex3z.flowlayout.FlowLayout
import com.robertlevonyan.views.chip.Chip
import io.github.javiewer.JAViewer
import io.github.javiewer.R
import io.github.javiewer.R.drawable
import io.github.javiewer.R.layout
import io.github.javiewer.adapter.ActressPaletteAdapter
import io.github.javiewer.adapter.MovieHeaderAdapter
import io.github.javiewer.adapter.ScreenshotAdapter
import io.github.javiewer.fragment.BaseFragment
import io.github.javiewer.model.entity.Genre
import io.github.javiewer.model.entity.Movie
import io.github.javiewer.model.entity.MovieDetail
import io.github.javiewer.model.entity.MovieDetail.Header
import io.github.javiewer.model.network.provider.AVMOProvider
import io.github.javiewer.view.ViewUtil
import kotlinx.android.synthetic.main.activity_movie.fab
import kotlinx.android.synthetic.main.activity_movie.movie_content
import kotlinx.android.synthetic.main.activity_movie.movie_progress_bar
import kotlinx.android.synthetic.main.activity_movie.toolbar
import kotlinx.android.synthetic.main.activity_movie.toolbar_layout_background
import kotlinx.android.synthetic.main.content_movie_genre.genre_flow_layout
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MovieDetailFragment : BaseFragment() {
  var movie: Movie? = null

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
      return inflater.inflate(R.layout.activity_movie, container, false)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.setTitle(movie!!.title)
    fab!!.setOnClickListener(OnClickListener {
      val intent = Intent(this@MovieDetailFragment, DownloadActivity::class.java)
      val arguments = Bundle()
      arguments.putString("keyword", movie!!.code)
      intent.putExtras(arguments)
      startActivity(intent)
    })
    fab!!.bringToFront()
    val call = JAViewer.SERVICE[movie!!.link]
    call!!.enqueue(object : Callback<ResponseBody> {
      override fun onResponse(
        call: Call<ResponseBody>,
        response: Response<ResponseBody>
      ) {
        if (!response.isSuccessful) {
          return
        }
        val detail: MovieDetail
        try {
          detail = AVMOProvider.parseMoviesDetail(
              response.body()!!
                  .string()
          )
          detail.headers.add(0, Header.create("影片名", movie!!.title, null))
          displayInfo(detail)
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

  private fun displayInfo(detail: MovieDetail) {
    //Info
    run {
      val recycler_view: RecyclerView = findViewById<View>(id.headers_recycler_view) as RecyclerView
      val mIcon: ImageView = findViewById<View>(id.movie_icon_header) as ImageView
      if (detail.headers.isEmpty()) {
        val mText: TextView = findViewById<View>(id.header_empty_text) as TextView
        recycler_view.setVisibility(View.GONE)
        mText.setVisibility(View.VISIBLE)
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
        recycler_view.setAdapter(MovieHeaderAdapter(detail.headers, this, mIcon))
        recycler_view.setLayoutManager(LinearLayoutManager(this))
        recycler_view.setNestedScrollingEnabled(false)
      }
    }

    //Screenshots
    run {
      val recycler_view: RecyclerView =
        findViewById<View>(id.screenshots_recycler_view) as RecyclerView
      val mIcon: ImageView = findViewById<View>(id.movie_icon_screenshots) as ImageView
      if (detail.screenshots.isEmpty()) {
        val mText: TextView = findViewById<View>(id.screenshots_empty_text) as TextView
        recycler_view.setVisibility(View.GONE)
        mText.setVisibility(View.VISIBLE)
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
        recycler_view.setAdapter(ScreenshotAdapter(detail.screenshots, this, mIcon, movie))
        recycler_view.setLayoutManager(
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        )
        recycler_view.setNestedScrollingEnabled(false)
      }
    }

    //Actress
    run {
      val recycler_view: RecyclerView =
        findViewById<View>(id.actresses_recycler_view) as RecyclerView
      val mIcon: ImageView = findViewById<View>(id.movie_icon_actresses) as ImageView
      if (detail.actresses.isEmpty()) {
        val mText: TextView = findViewById<View>(id.actresses_empty_text) as TextView
        recycler_view.setVisibility(View.GONE)
        mText.setVisibility(View.VISIBLE)
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
        recycler_view.setAdapter(ActressPaletteAdapter(detail.actresses, this, mIcon))
        recycler_view.setLayoutManager(
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        )
        recycler_view.setNestedScrollingEnabled(false)
      }
    }

    //Genre
    run {
      val mIcon: ImageView = findViewById<View>(id.movie_icon_genre) as ImageView
      if (detail.genres.isEmpty()) {
        genre_flow_layout!!.visibility = View.GONE
        val mText: TextView = findViewById<View>(id.genre_empty_text) as TextView
        mText.visibility = View.VISIBLE
        ViewUtil.alignIconToView(mIcon, mText)
      } else {
        for (i in detail.genres.indices) {
          val genre: Genre = detail.genres.get(i)
          val view: View = layoutInflater.inflate(layout.chip_genre, genre_flow_layout, false)
          val chip: Chip = view.findViewById<View>(id.chip_genre) as Chip
          chip.setOnClickListener {
            if (genre.link != null) {
              startActivity(
                  MovieListActivity.newIntent(
                      this@MovieDetailFragment, genre.name, genre.link
                  )
              )
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
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.movie, menu)
    val mStarButton = menu.findItem(R.id.action_star)
    run {
      if (JAViewer.CONFIGURATIONS.starredMovies
              .contains(movie)
      ) {
        mStarButton!!.setIcon(drawable.ic_menu_star)
        mStarButton!!.title = "取消收藏"
      }
    }
    mStarButton.setOnMenuItemClickListener {
      if (JAViewer.CONFIGURATIONS.starredMovies.contains(movie)) {
        JAViewer.CONFIGURATIONS.starredMovies.remove(movie)
        mStarButton.setIcon(drawable.ic_menu_star_border)
        Snackbar.make((movie_content)!!, "已取消收藏", Snackbar.LENGTH_LONG)
            .show()
        mStarButton.setTitle("收藏")
      } else {
        val movies: MutableList<Movie?> = JAViewer.CONFIGURATIONS.starredMovies.toMutableList()
        movies.reverse()
        movies.add(movie)
        movies.reverse()
        mStarButton.setIcon(drawable.ic_menu_star)
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
          val cache = File(getExternalFilesDir("cache"), "screenshot")
          //Generate screenshot
          val os = FileOutputStream(cache)
          val screenshot = screenBitmap
          screenshot.compress(JPEG, 100, os)
          os.flush()
          os.close()
          val uri =
            FileProvider.getUriForFile(applicationContext, "io.github.javiewer.fileprovider", cache)
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
          Toast.makeText(this@MovieDetailFragment, "无法分享：" + e.message, Toast.LENGTH_SHORT)
              .show()
        }
        return false
      }
    })
    return super.onCreateOptionsMenu(menu)
  }
  //Image

  //ScrollView
//  val screenBitmap: Bitmap
//    get() {
//      val imageHeight = toolbar_layout_background!!.height
//      var scrollViewHeight = 0
//      for (i in 0 until movie_content!!.childCount) {
//        scrollViewHeight += movie_content!!.getChildAt(i).height
//      }
//      val result = Bitmap.createBitmap(movie_content!!.width, imageHeight + scrollViewHeight, ARGB_8888)
//      val canvas = Canvas(result)
//      canvas.drawColor(Color.parseColor("#FAFAFA"))
//
//      //Image
//      run {
//        val bitmap: Bitmap =
//          Bitmap.createBitmap(toolbar_layout_background!!.getWidth(), imageHeight, ARGB_8888)
//        val c: Canvas = Canvas(bitmap)
//        toolbar_layout_background!!.draw(c)
//        canvas.drawBitmap(bitmap, 0f, 0f, null)
//      }
//
//      //ScrollView
//      run {
//        val bitmap: Bitmap = Bitmap.createBitmap(movie_content!!.getWidth(), scrollViewHeight, ARGB_8888)
//        val c: Canvas = Canvas(bitmap)
//        movie_content!!.draw(c)
//        canvas.drawBitmap(bitmap, 0f, imageHeight.toFloat(), null)
//      }
//      return result
//    }

//  @OnClick(id.view_preview) fun onClickPreview() {
//    //TODO: Deprecated
//    if (video != null) {
//      JZVideoPlayerStandard.startFullscreen(
//          this@MovieDetailFragment, SimpleVideoPlayer::class.java, video!!.preview_video_url,
//          movie!!.title
//      )
//      return
//    }
//    val dialog = ProgressDialog.show(this, "请稍后", "正在搜索该影片的预览视频", true, false)
//    PSVS.INSTANCE.search(movie!!.code)
//        ?.enqueue(object : Callback<AvgleSearchResult?> {
//          override fun onResponse(
//            call: Call<AvgleSearchResult?>,
//            response: Response<AvgleSearchResult?>
//          ) {
//            if (response.isSuccessful) {
//              val result = response.body()
//              if (result!!.success && !result.response?.videos.isNullOrEmpty()) {
//                video = result.response!!.videos!![0]
//                JZVideoPlayerStandard.startFullscreen(
//                    this@MovieDetailFragment, SimpleVideoPlayer::class.java,
//                    video!!.preview_video_url,
//                    movie!!.title
//                )
//                Toast.makeText(this@MovieDetailFragment, "提示：预览视频可能需要科学上网", Toast.LENGTH_LONG)
//                    .show()
//                dialog.dismiss()
//                return
//              }
//            }
//            Toast.makeText(this@MovieDetailFragment, "该影片暂无预览", Toast.LENGTH_LONG)
//                .show()
//            dialog.dismiss()
//          }
//
//          override fun onFailure(
//            call: Call<AvgleSearchResult?>,
//            t: Throwable
//          ) {
//            t.printStackTrace()
//            Toast.makeText(this@MovieDetailFragment, "获取预览失败，请重试，或使用科学上网", Toast.LENGTH_LONG)
//                .show()
//            dialog.dismiss()
//          }
//        })
//  }

//  @OnClick(id.view_play)
//  fun onPlay() {
//    //TODO: Deprecated
//    val ts = (System.currentTimeMillis() / 1000).toString()
//    if (video != null) {
//      JZVideoPlayerStandard.startFullscreen(
//          this@MovieDetailFragment,
//          SimpleVideoPlayer::class.java, String.format(
//          "http://api.rekonquer.com/psvs/mp4.php?vid=%s&ts=%s&sign=%s", video!!.vid, ts, JAViewer.b(
//          video!!.vid, ts
//      )
//      ),
//          movie!!.title
//      )
//      return
//    }
//    val dialog = ProgressDialog.show(this, "请稍后", "正在搜索该影片的在线视频源", true, false)
//    val call = PSVS.INSTANCE.search(movie!!.code)
//    call?.enqueue(object : Callback<AvgleSearchResult?> {
//      override fun onResponse(
//        call: Call<AvgleSearchResult?>,
//        response: Response<AvgleSearchResult?>
//      ) {
//        if (response.isSuccessful) {
//          val result = response.body()
//          if (result!!.success && !result.response?.videos.isNullOrEmpty()) {
//            video = result.response!!.videos!![0]
//            JZVideoPlayerStandard.startFullscreen(
//                this@MovieDetailFragment,
//                SimpleVideoPlayer::class.java, String.format(
//                "http://api.rekonquer.com/psvs/mp4.php?vid=%s&ts=%s&sign=%s", video!!.vid, ts,
//                JAViewer.b(video!!.vid, ts)
//            ),
//                movie!!.title
//            )
//            dialog.dismiss()
//            return
//          }
//        }
//        Toast.makeText(this@MovieDetailFragment, "该影片暂无在线视频源", Toast.LENGTH_LONG)
//            .show()
//        dialog.dismiss()
//      }
//
//      override fun onFailure(
//        call: Call<AvgleSearchResult?>,
//        t: Throwable
//      ) {
//        t.printStackTrace()
//        Toast.makeText(this@MovieDetailFragment, "获取在线视频源失败，请重试，或使用科学上网", Toast.LENGTH_LONG)
//            .show()
//        dialog.dismiss()
//      }
//    })
//  }

  /*@OnClick(R.id.view_play)
    public void onPlay() {
        //TODO: Deprecated
        final ProgressDialog dialog = ProgressDialog.show(this, "请稍后", "正在搜索该影片的在线视频源", true, false);

        if (video != null) {
            dialog.setMessage("正在获取播放地址");

            String ts = String.valueOf(System.currentTimeMillis() / 1000);
            Request request = new Request.Builder()
                    .url(String.format(
                            "https://avgle.com/mp4.php?vid=%s&ts=%s&hash=%s&m3u8"
                            , video.vid
                            , ts
                            , PSVS21.computeHash(new PSVS21.StubContext(MovieActivity.this.getApplicationContext()), video.vid, ts)))
                    .build();
            JAViewer.HTTP_CLIENT.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    dialog.dismiss();
                    Toast.makeText(MovieActivity.this, "获取播放地址失败，请尝试科学上网", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    dialog.dismiss();
                    startFullscreen(response.request().url().toString(), movie.title);
                }
            });
            //startActivityForResult(WebViewActivity.newIntent(MovieActivity.this, video.embedded_url), 0x0000eeff);
            return;
        }

        Call<AvgleSearchResult> call = Avgle.INSTANCE.search(movie.code);
        call.enqueue(new Callback<AvgleSearchResult>() {
            @Override
            public void onResponse(Call<AvgleSearchResult> call, Response<AvgleSearchResult> response) {
                if (response.isSuccessful()) {
                    AvgleSearchResult result = response.body();
                    if (result.success && result.response.videos.size() > 0) {
                        video = result.response.videos.get(0);
                        //startActivityForResult(WebViewActivity.newIntent(MovieActivity.this, video.embedded_url), 0x0000eeff);
                        //dialog.dismiss();
                        dialog.setMessage("正在获取播放地址");

                        String ts = String.valueOf(System.currentTimeMillis() / 1000);
                        Request request = new Request.Builder()
                                .url(String.format(
                                        "https://avgle.com/mp4.php?vid=%s&ts=%s&hash=%s&m3u8"
                                        , video.vid
                                        , ts
                                        , PSVS21.computeHash(new PSVS21.StubContext(MovieActivity.this.getApplicationContext()), video.vid, ts)))
                                .build();
                        JAViewer.HTTP_CLIENT.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                dialog.dismiss();
                                Toast.makeText(MovieActivity.this, "获取播放地址失败，请尝试科学上网", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                dialog.dismiss();
                                startFullscreen(response.request().url().toString(), movie.title);
                            }
                        });
                        return;
                    }
                }

                Toast.makeText(MovieActivity.this, "该影片暂无在线视频源", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<AvgleSearchResult> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MovieActivity.this, "获取视频源失败，请尝试科学上网", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }*/
  /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x0000eeff && resultCode == RESULT_OK) {
            JZVideoPlayerStandard.startFullscreen(MovieActivity.this, SimpleVideoPlayer.class, data.getStringExtra("m3u8"), movie.title);
        }
    }*/
//  fun startFullscreen(
//    url: String?,
//    title: String?
//  ) {
//    Handler(Looper.getMainLooper()).post(object : Runnable {
//      override fun run() {
//        JZVideoPlayerStandard.startFullscreen(
//            this@MovieDetailFragment, SimpleVideoPlayer::class.java, url, title
//        )
//      }
//    })
//  }

  override fun onDestroy() {
    super.onDestroy()
    JZVideoPlayer.releaseAllVideos()
  }
}