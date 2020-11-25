package io.github.javiewer.network

import io.github.javiewer.JAViewer
import io.github.javiewer.network.item.AvgleSearchResult
import retrofit2.Call
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Project: JAViewer
 */
interface PSVS {
  @GET("/psvs/search.php") fun search(
    @Query(
        value = "kw"
    ) keyword: String?
  ): Call<AvgleSearchResult?>?

  companion object {
    const val BASE_URL = "http://api.rekonquer.com"
    val INSTANCE = Builder()
        .baseUrl(BASE_URL)
        .client(JAViewer.HTTP_CLIENT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PSVS::class.java)
  }
}