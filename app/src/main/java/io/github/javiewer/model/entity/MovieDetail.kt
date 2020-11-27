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
  class Header : Linkable() {
    @JvmField var name: String? = null
    @JvmField var value: String? = null
    override fun toString(): String {
      return "Header{" +
          "name='" + name + '\'' +
          ", value='" + value + '\'' +
          '}'
    }

    companion object {
      fun create(
        name: String?,
        value: String?,
        link: String?
      ): Header {
        val header = Header()
        header.name = name
        header.value = value
        header.link = link
        return header
      }
    }
  }
}