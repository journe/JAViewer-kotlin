package io.github.javiewer.adapter.item

/**
 * Project: JAViewer
 */
class DataSource(
  var name: String,
  baseUrl: String?
) : Linkable() {
  var legacies: List<String>? = null
  override fun toString(): String {
    return name
  }

  companion object {
    var AVMO = DataSource("AVMOO 日本", "https://avos.pw")
    var AVSO = DataSource("AVSOX 日本无码", "https://avso.club")
    var AVXO = DataSource("AVMEMO 欧美", "https://avxo.pw")
  }

  init {
    link = baseUrl
  }
}