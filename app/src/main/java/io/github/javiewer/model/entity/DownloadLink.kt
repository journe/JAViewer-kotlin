package io.github.javiewer.model.entity

/**
 * Project: JAViewer
 */
class DownloadLink : Linkable() {
  var title: String? = null
    protected set
  var size: String? = null
    protected set
  var date: String? = null
    protected set
  protected var magnetLink: MagnetLink? = null
  fun hasMagnetLink(): Boolean {
    return magnetLink?.magnetLink != null
  }

  fun getMagnetLink(): String? {
    return magnetLink?.magnetLink
  }

  companion object {
    fun create(
      title: String?,
      size: String?,
      date: String?,
      link: String?,
      magnetLink: String?
    ): DownloadLink {
      val download = DownloadLink()
      download.title = title
      download.size = size
      download.date = date
      download.link = link
      download.magnetLink = MagnetLink.create(magnetLink)
      return download
    }
  }
}