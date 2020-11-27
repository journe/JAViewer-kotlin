package io.github.javiewer.model.entity

import java.io.Serializable

/**
 * Project: JAViewer
 */
class MagnetLink : Serializable {
  var magnetLink: String? = null
    protected set

  companion object {
    fun create(magnetLink: String?): MagnetLink {
      val magnet = MagnetLink()
      if (magnetLink != null) {
        magnet.magnetLink = magnetLink.substring(0, magnetLink.indexOf("&"))
      }
      return magnet
    }
  }
}