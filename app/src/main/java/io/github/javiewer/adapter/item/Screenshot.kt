package io.github.javiewer.adapter.item

/**
 * Project: JAViewer
 */
class Screenshot : Linkable() {
  var thumbnailUrl: String? = null
    protected set
  val imageUrl: String?
    get() = link

  companion object {
    fun create(
      thumbnailUrl: String?,
      imageUrl: String?
    ): Screenshot {
      val screenshot = Screenshot()
      screenshot.thumbnailUrl = thumbnailUrl
      screenshot.link = imageUrl
      return screenshot
    }
  }
}