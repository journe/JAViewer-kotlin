package io.github.javiewer.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project: JAViewer
 */
@Entity
data class Movie(
  val title: String?,
  @PrimaryKey
  val code: String?,
  val date: String?,
  val coverUrl: String?,
  val link: String?,
  val hot: Boolean = false
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