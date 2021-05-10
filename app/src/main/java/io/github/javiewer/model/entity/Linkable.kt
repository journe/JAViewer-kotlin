package io.github.javiewer.model.entity

import io.github.javiewer.JAViewer.Companion.Objects_equals
import java.io.Serializable

/**
 * Project: JAViewer
 */
open class Linkable {
  var link: String? = null
  override fun toString(): String {
    return "Linkable{" +
        "link='" + link + '\'' +
        '}'
  }
}