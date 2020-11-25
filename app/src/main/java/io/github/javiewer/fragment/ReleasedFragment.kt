package io.github.javiewer.fragment

import io.github.javiewer.JAViewer
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project: JAViewer
 */
class ReleasedFragment : MovieFragment() {
  override fun newCall(page: Int): Call<ResponseBody> {
    return JAViewer.SERVICE.getReleased(page)
  }
}