package io.github.javiewer.model.network

import com.orhanobut.logger.Logger
import io.github.javiewer.BuildConfig
import io.github.javiewer.util.JsonUtil
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by journey on 2020/11/26.
 */
object BasicRetrofit {
  const val USER_AGENT =
    "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"

  val HTTP_CLIENT: OkHttpClient = Builder().apply {
    addInterceptor { chain ->
      val original = chain.request()
      val request = original.newBuilder()
          .header("User-Agent", USER_AGENT)
          .build()
      chain.proceed(request)
    }
    cookieJar(object : CookieJar {
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
    if (BuildConfig.DEBUG) {
      addNetworkInterceptor(HttpLoggingInterceptor(HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY))
    }
  }.build()

  private class HttpLogger : HttpLoggingInterceptor.Logger {
    private val mMessage = StringBuffer() //try to resolve the arrayIndexOutOfBoundsException
    override fun log(message: String) { // 请求或者响应开始
      var message = message
      if (message.startsWith("--> POST")) {
        mMessage.setLength(0)
      }
      // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
      if (message.startsWith("{") && message.endsWith("}")
          || message.startsWith("[") && message.endsWith("]")
      ) {
        message = JsonUtil.formatJson(message)
      }
      mMessage.append(message)
      mMessage.append("\n")
      // 请求或者响应结束，打印整条日志
      if (message.startsWith("<-- END HTTP")) {
        Logger.t("apilog")
            .d(mMessage.toString())
        mMessage.setLength(0)
      }
    }
  }
}