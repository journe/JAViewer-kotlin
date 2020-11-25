package io.github.javiewer.network.provider

import io.github.javiewer.adapter.item.DownloadLink
import io.github.javiewer.adapter.item.MagnetLink
import io.github.javiewer.network.TorrentKitty
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import java.util.ArrayList

/**
 * Project: JAViewer
 */
class TorrentKittyLinkProvider : DownloadLinkProvider() {
  override fun search(
    keyword: String?,
    page: Int
  ): Call<ResponseBody?>? {
    return if (page == 1) {
      TorrentKitty.INSTANCE.search(keyword)
    } else {
      null
    }
  }

  override fun parseDownloadLinks(htmlContent: String?): List<DownloadLink> {
    val links = ArrayList<DownloadLink>()
    val table = Jsoup.parse(htmlContent)
        .getElementById("archiveResult")
    for (tr in table.getElementsByTag("tr")) {
      try {
        links.add(
            DownloadLink.create(
                tr.getElementsByClass("name")
                    .first()
                    .text(),
                "",
                tr.getElementsByClass("date")
                    .first()
                    .text(),
                null,
                tr.getElementsByAttributeValue("rel", "magnet")
                    .first()
                    .attr("href")
            )
        )
      } catch (ignored: Exception) {
      }
    }
    return links
  }

  override fun get(url: String?): Call<ResponseBody?>? {
    return null
    //ABANDONED
  }

  override fun parseMagnetLink(htmlContent: String?): MagnetLink? {
    return null
    //ABANDONED
  }
}