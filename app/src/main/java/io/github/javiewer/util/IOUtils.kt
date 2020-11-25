package io.github.javiewer.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * Created by ipcjs on 2020/8/23
 */
object IOUtils {
  val UTF_8 = Charset.forName("utf-8")
  @Throws(IOException::class) fun readText(
    `is`: InputStream?,
    charset: Charset?
  ): String {
    val br = BufferedReader(InputStreamReader(`is`, charset))
    val sb = StringBuilder()
    var line: String?
    while (br.readLine()
            .also { line = it } != null
    ) {
      sb.append(line)
    }
    return sb.toString()
  }
}