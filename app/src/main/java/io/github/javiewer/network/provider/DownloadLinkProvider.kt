package io.github.javiewer.network.provider

import io.github.javiewer.adapter.item.DownloadLink
import io.github.javiewer.adapter.item.MagnetLink
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project: JAViewer
 */
abstract class DownloadLinkProvider {
  abstract fun search(
    keyword: String?,
    page: Int
  ): Call<ResponseBody?>?

  abstract fun parseDownloadLinks(htmlContent: String?): List<DownloadLink>
  abstract operator fun get(url: String?): Call<ResponseBody?>?
  abstract fun parseMagnetLink(htmlContent: String?): MagnetLink?

  companion object {
    fun getProvider(name: String): DownloadLinkProvider? {
      return when (name.toLowerCase()
          .trim { it <= ' ' }) {
        "btso" -> BTSOLinkProvider()
        "torrentkitty" -> TorrentKittyLinkProvider()
        else -> null
      }
    }
  }
}