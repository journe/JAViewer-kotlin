package io.github.javiewer.model.entity

/**
 * Project: JAViewer
 */
data class MovieDetail(
  val screenshots: MutableList<Screenshot> = mutableListOf(),
  var title: String? = null,
  var coverUrl: String? = null,
  var headers: MutableList<Header> = mutableListOf(),
  var genres: MutableList<Genre> = mutableListOf(),
  var actresses: MutableList<Actress> = mutableListOf()
) {
  data class Header(
    val name: String?,
    val value: String?,
    val link: String?
  )

  data class Genre(
    val name: String?,
    val link: String?
  )

  data class Actress(
    val name: String?,
    val imageUrl: String?,
    val detailUrl: String?
  )

  data class Screenshot(
    val thumbnailUrl: String?,
    val imageUrl: String?
  )
}

