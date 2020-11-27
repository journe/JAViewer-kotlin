package io.github.javiewer.model.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * Project: JAViewer
 */
interface BasicService {
  @GET("$LANGUAGE_NODE/page/{page}")
  fun getHomePage(@Path("page") page: Int): Call<ResponseBody>

  @GET("$LANGUAGE_NODE/released/page/{page}")
  fun getReleased(@Path("page") page: Int): Call<ResponseBody>

  @GET("$LANGUAGE_NODE/popular/page/{page}")
  fun getPopular(@Path("page") page: Int): Call<ResponseBody>

  @GET("$LANGUAGE_NODE/actresses/page/{page}")
  fun getActresses(@Path("page") page: Int): Call<ResponseBody>

  @GET("$LANGUAGE_NODE/genre")
  fun getGenres(): Call<ResponseBody>

  @GET
  operator fun get(@Url url: String?): Call<ResponseBody>

  companion object {
    const val LANGUAGE_NODE = "/cn"
  }
}