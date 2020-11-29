package io.github.javiewer.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project: JAViewer
 */
@Entity
data class Movie(
  val title: String?,//标题
  @PrimaryKey
  val code: String = "",//番号
  val date: String?,//发布日期
  val coverUrl: String?,//封面链接
  val link: String?,//详情链接
  val hot: Boolean = false,//是否热门
  val page: Int = 0//本地用参数，此Item在请求的第几页
) {
  override fun toString(): String {
    return "Movie{" +
        "title='" + title + '\'' +
        ", code='" + code + '\'' +
        ", coverUrl='" + coverUrl + '\'' +
        ", link='" + link + '\'' +
        ", date='" + date + '\'' +
        ", hot=" + hot +
        '}'
  }
}