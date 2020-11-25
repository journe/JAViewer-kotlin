package io.github.javiewer

import android.app.Application
import android.os.Environment
import cn.jzvd.JZVideoPlayer
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.github.javiewer.adapter.item.DataSource
import io.github.javiewer.network.BasicService
import io.github.javiewer.util.ExoPlayerImpl
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.ArrayList
import java.util.HashMap

/**
 * Project: JAViewer
 */
class JAViewer : Application() {
  companion object {
    const val USER_AGENT =
      "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"
    val DATA_SOURCES: MutableList<DataSource> = mutableListOf()
    lateinit var CONFIGURATIONS: Configurations
    lateinit var SERVICE: BasicService

    val hostReplacements: MutableMap<String, String?> = HashMap()
    val HTTP_CLIENT: OkHttpClient = Builder().addInterceptor { chain ->
      val original = chain.request()
      val request = original.newBuilder()
          .url(replaceUrl(original.url()))
          .header("User-Agent", USER_AGENT)
          .build()
      chain.proceed(request)
    }
        .cookieJar(object : CookieJar {
          private val cookieStore = HashMap<HttpUrl, List<Cookie>>()
          override fun saveFromResponse(
            url: HttpUrl,
            cookies: List<Cookie>
          ) {
            cookieStore[url] = cookies
          }

          override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val cookies = cookieStore[url]
            return cookies ?: ArrayList()
          }
        })
        .build()
    val dataSource: DataSource
      get() = CONFIGURATIONS.dataSource

    fun recreateService() {
      SERVICE = Retrofit.Builder()
          .baseUrl(dataSource.getLink())
          .client(HTTP_CLIENT)
          .build()
          .create(BasicService::class.java)
    }

    val storageDir: File
      get() {
        val dir = File(Environment.getExternalStorageDirectory(), "JAViewer/")
        dir.mkdirs()
        return dir
      }

    private fun replaceUrl(url: HttpUrl): HttpUrl {
      val builder = url.newBuilder()
      val host = url.url().host
      if (hostReplacements.containsKey(host)) {
        builder.host(hostReplacements[host])
        return builder.build()
      }
      return url
    }

    @Throws(JsonParseException::class) fun <T> parseJson(
      beanClass: Class<T>?,
      reader: JsonReader?
    ): T {
      val builder = GsonBuilder()
      val gson = builder.create()
      return gson.fromJson(reader, beanClass)
    }

    @Throws(JsonParseException::class) fun <T> parseJson(
      beanClass: Class<T>?,
      json: String?
    ): T {
      val builder = GsonBuilder()
      val gson = builder.create()
      return gson.fromJson(json, beanClass)
    }

    fun bytesToHex(bytes: ByteArray): String {
      val hexArray =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
      val hexChars = CharArray(bytes.size * 2)
      var v: Int
      for (j in bytes.indices) {
        v = bytes[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
      }
      return String(hexChars)
    }

    @JvmStatic fun Objects_equals(
      a: Any?,
      b: Any?
    ): Boolean {
      return a == b
    }

    fun b(
      s1: String?,
      s2: String?
    ): String? {
      return try {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(
            String.format("%s%sBrynhildr", s1, s2)
                .toByteArray()
        )
        bytesToHex(bytes)
      } catch (e: NoSuchAlgorithmException) {
        null
      }
    }

    init {
      JZVideoPlayer.setMediaInterface(ExoPlayerImpl())
    }
  }

  override fun onCreate() {
    super.onCreate()
    Logger.addLogAdapter(object :
        AndroidLogAdapter(
            PrettyFormatStrategy.newBuilder().tag("JAV").build()) {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return BuildConfig.DEBUG
      }
    })
  }
}