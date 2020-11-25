package io.github.javiewer.network

import io.github.javiewer.JAViewer
import io.github.javiewer.network.item.AvgleSearchResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Project: JAViewer
 */
interface Avgle {
  @GET("/v1/search/{keyword}/0?limit=1") @Headers("Accept-Language: zh-CN,zh;q=0.8,en;q=0.6")
  fun search(@Path(value = "keyword") keyword: String?): Call<AvgleSearchResult?>?
  @GET("/{path}") operator fun get(@Path("path") path: String?): Call<ResponseBody?>?

  companion object {
    const val BASE_URL = "https://api.avgle.com"
    val INSTANCE = Builder()
        .baseUrl(BASE_URL)
        .client(JAViewer.HTTP_CLIENT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Avgle::class.java)
  }
}