package io.github.javiewer.fragment

import android.os.Bundle
import io.github.javiewer.JAViewer
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project: JAViewer
 */
class MovieListFragment : MovieFragment() {
  var link: String? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val bundle = arguments
    link = bundle!!.getString("link")
  }

  override fun newCall(page: Int): Call<ResponseBody> {
    return JAViewer.SERVICE[link + "/page/" + page]
  }
}