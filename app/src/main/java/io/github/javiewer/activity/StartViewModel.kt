package io.github.javiewer.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import org.jsoup.Jsoup

/**
 * Created by journey on 2020/11/25.
 */
class StartViewModel : ViewModel() {
  val tellme = liveData {
    emit(
        "https://avmoo.cyou"
//        getTellMe()
    )
  }

  private suspend fun getTellMe(): String =
    Dispatchers.IO {
      val ele = Jsoup.connect("https://tellme.pw/avmoo")
          .get()
      return@IO ele.body()
          .selectFirst("h4")
          .text()
    }

}