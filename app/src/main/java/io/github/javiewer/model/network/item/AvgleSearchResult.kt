package io.github.javiewer.model.network.item

import com.google.gson.annotations.SerializedName

/**
 * Project: JAViewer
 */
class AvgleSearchResult {
  var success = false
  var response: Response? = null

  class Response {
    var has_more = false
    var total_videos = 0
    var current_offset = 0
    var limit = 0
    var videos: List<Video>? = null

    class Video {
      var title: String? = null
      var keyword: String? = null
      var channel: String? = null
      var duration = 0.0
      var framerate = 0.0
      var hd = false
      var addtime = 0
      var viewnumber = 0
      var likes = 0
      var dislikes = 0
      var video_url: String? = null
      var embedded_url: String? = null
      var preview_url: String? = null
      var preview_video_url: String? = null

      @SerializedName("public")
      var isPublic = false
      var vid: String? = null
      var uid: String? = null
    }
  }
}