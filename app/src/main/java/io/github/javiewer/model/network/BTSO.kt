package io.github.javiewer.model.network

import io.github.javiewer.JAViewer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit.Builder
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Project: JAViewer
 */
interface BTSO {
  @GET("/btso.php") @Headers("Accept-Language: zh-CN,zh;q=0.8,en;q=0.6") fun search(
    @Query(
        value = "kw"
    ) keyword: String?,
    @Query("page") page: Int
  ): Call<ResponseBody?>?

  @GET @Headers("Accept-Language: zh-CN,zh;q=0.8,en;q=0.6")
  operator fun get(@Url url: String?): Call<ResponseBody?>?

  companion object {
    const val BASE_URL = "https://api.rekonquer.com"
    val INSTANCE = Builder()
        .baseUrl(BASE_URL)
        .client(JAViewer.HTTP_CLIENT)
        .build()
        .create(BTSO::class.java)
  }
}