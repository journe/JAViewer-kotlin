package io.github.javiewer

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.github.javiewer.adapter.item.Actress
import io.github.javiewer.adapter.item.DataSource
import io.github.javiewer.adapter.item.Movie
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.ArrayList

/**
 * Project: JAViewer
 */
class Configurations {
  private var starred_movies: ArrayList<Movie>? = null
  private var starred_actresses: ArrayList<Actress>? = null
  private var data_source: DataSource? = null
  private var show_ads = false
  var downloadCounter: Long = 0
  val starredMovies: ArrayList<Movie>
    get() {
      if (starred_movies == null) {
        starred_movies = ArrayList()
      }
      return starred_movies!!
    }
  val starredActresses: ArrayList<Actress>
    get() {
      if (starred_actresses == null) {
        starred_actresses = ArrayList()
      }
      return starred_actresses!!
    }
  var dataSource: DataSource
    get() {
      if (data_source == null) {
        data_source = JAViewer.DATA_SOURCES[0]
      }
      return data_source!!
    }
    set(source) {
      data_source = source
    }

  fun save() {
    try {
      val writer = FileWriter(file)
      Gson().toJson(this, writer)
      writer.flush()
      writer.close()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  fun setShowAds(show_ads: Boolean) {
    this.show_ads = show_ads
  }

  fun showAds(): Boolean {
    return show_ads
  }

  companion object {
    private var file: File? = null
    fun load(file: File?): Configurations {
      Companion.file = file
      var config: Configurations? = null
      try {
        config = JAViewer.parseJson(
            Configurations::class.java, JsonReader(
            FileReader(file)
        )
        )
      } catch (ignored: Exception) {
      }
      if (config == null) {
        config = Configurations()
      }
      return config
    }
  }
}