package io.github.javiewer.util.common

import android.content.Context
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object JsonUtil {
  /**
   * 得到json文件中的内容
   * @param context
   * @param fileName
   * @return
   */
  fun getJson(
    context: Context,
    fileName: String?
  ): String {
    val stringBuilder = StringBuilder()
    //获得assets资源管理器
    val assetManager = context.assets
    //使用IO流读取json文件内容
    try {
      val bufferedReader = BufferedReader(
          InputStreamReader(
              assetManager.open(fileName), "utf-8"
          )
      )
      var line: String?
      while (bufferedReader.readLine()
              .also { line = it } != null
      ) {
        stringBuilder.append(line)
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return stringBuilder.toString()
  }

  /**
   * 将字符串转换为 对象
   * @param json
   * @param type
   * @return
   */
  fun <T> JsonToObject(
    json: String?,
    type: Class<T>?
  ): T {
    val gson = Gson()
    return gson.fromJson(json, type)
  }

  /**
   * 格式化json字符串
   *
   * @param jsonStr 需要格式化的json串
   * @return 格式化后的json串
   */
  fun formatJson(jsonStr: String?): String {
    if (null == jsonStr || "" == jsonStr) return ""
    val sb = StringBuilder()
    var last = '\u0000'
    var current = '\u0000'
    var indent = 0
    for (i in 0 until jsonStr.length) {
      last = current
      current = jsonStr[i]
      when (current) {
        '{', '[' -> {
          sb.append(current)
          sb.append('\n')
          indent++
          addIndentBlank(sb, indent)
        }
        '}', ']' -> {
          sb.append('\n')
          indent--
          addIndentBlank(sb, indent)
          sb.append(current)
        }
        ',' -> {
          sb.append(current)
          if (last != '\\') {
            sb.append('\n')
            addIndentBlank(sb, indent)
          }
        }
        else -> sb.append(current)
      }
    }
    return sb.toString()
  }

  /**
   * 添加space
   *
   * @param sb
   * @param indent
   */
  private fun addIndentBlank(
    sb: StringBuilder,
    indent: Int
  ) {
    for (i in 0 until indent) {
      sb.append('\t')
    }
  }

}