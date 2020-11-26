package io.github.javiewer.adapter.item

/**
 * Project: JAViewer
 */
class Actress : Linkable() {
  var name: String? = null
    protected set
  var imageUrl: String? = null
    protected set

  override fun equals(obj: Any?): Boolean {
    if (super.equals(obj)) {
      return true
    }
    return if (obj is Actress) {
      name == obj.name
    } else false
  }

  companion object {
    fun create(
      name: String?,
      imageUrl: String?,
      detailUrl: String?
    ): Actress {
      val actress = Actress()
      actress.name = name
      actress.imageUrl = imageUrl
      actress.link = detailUrl
      return actress
    }
  }
}