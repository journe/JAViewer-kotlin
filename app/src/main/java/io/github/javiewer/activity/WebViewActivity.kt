package io.github.javiewer.activity

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.webkit.CookieManager
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.github.javiewer.R.id
import io.github.javiewer.R.layout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import java.io.IOException

class WebViewActivity constructor() : SecureActivity() {
  @BindView(id.web_view)
  var mWebView: WebView? = null
  var embeddedUrl: String? = null
  var locked: Boolean = true
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_web_view)
    ButterKnife.bind(this)
    val bundle: Bundle = getIntent().getExtras()
    embeddedUrl = bundle.getString("embedded_url")
    val toolbar: Toolbar = findViewById(id.toolbar)
    setSupportActionBar(toolbar)
    val cookieManager: CookieManager = CookieManager.getInstance()
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      cookieManager.setAcceptThirdPartyCookies(mWebView, true)
    }
    val webSettings: WebSettings = mWebView!!.getSettings()
    webSettings.setJavaScriptEnabled(true)
    mWebView!!.setOnTouchListener(object : OnTouchListener {
      public override fun onTouch(
        v: View,
        event: MotionEvent
      ): Boolean {
        return locked
      }
    })
    mWebView!!.setWebViewClient(object : WebViewClient() {
      public override fun shouldInterceptRequest(
        view: WebView,
        url: String
      ): WebResourceResponse? {
        if (url.contains("?hash=")) {
          val cookie: String = cookieManager.getCookie(url)
          val request: Request = Builder()
              .url(url)
              .header("Referer", "https://javiewer.github.io/")
              .header("Cookie", cookie)
              .get()
              .build()
          httpClient.newCall(request)
              .enqueue(object : Callback {
                public override fun onFailure(
                  call: Call,
                  e: IOException
                ) {
                }

                @Throws(
                    IOException::class
                ) public override fun onResponse(
                  call: Call,
                  response: Response
                ) {
                  if (isFinishing()) {
                    return
                  }
                  val gson: Gson = Gson()
                  val json: String = response.body()!!
                      .string()
                  val `object`: JsonObject = gson.fromJson(json, JsonObject::class.java)
                  val playBack: String = `object`.get("url")
                      .getAsString()
                  testVideoPlayBack(playBack)
                  //Log.i("VideoPlayBack", playBack);
                }
              })
        }
        return super.shouldInterceptRequest(view, url)
      }
    })
    mWebView!!.loadDataWithBaseURL(
        "http://javiewer.github.io/",
        "<iframe width=\"100%\" height=\"100%\" src=\"" + embeddedUrl + "\" frameborder=\"0\" allowfullscreen></iframe>",
        "text/html",
        null,
        null
    )
  }

  fun testVideoPlayBack(url: String?) {
    val request: Request = Builder()
        .url(url)
        .get()
        .build()
    httpClient.newCall(request)
        .enqueue(object : Callback {
          public override fun onFailure(
            call: Call,
            e: IOException
          ) {
          }

          @Throws(IOException::class) public override fun onResponse(
            call: Call,
            response: Response
          ) {
            if (isFinishing()) {
              return
            }
            if (response.code() == 200) {
              val intent: Intent = Intent()
              intent.putExtra(
                  "m3u8", response.request()
                  .url()
                  .toString()
              )
              setResult(RESULT_OK, intent)
              finish()
            }
          }
        })
  }

  @OnClick(id.button_unlock) fun onUnlock(button: Button) {
    locked = false
    button.setEnabled(false)
    Toast.makeText(this, "锁定已解除，请完成验证码，不要按任何其他地方！", Toast.LENGTH_LONG)
        .show()
  }

  public override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  companion object {
    var httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
          @Throws(IOException::class) public override fun intercept(chain: Chain): Response {
            val original: Request = chain.request()
            val request: Request = original.newBuilder()
                .header("Connection", " keep-alive")
                .header("Accept", " */*")
                .header("X-Requested-With", " XMLHttpRequest")
                .header(
                    "User-Agent",
                    " Mozilla/5.0 (Windows NT 10.0 Win64 x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"
                )
                .header("Accept-Language", "zhCN,zh;q=0.8,en-US;q=0.5,cnq=0.3")
                .build()
            return chain.proceed(request)
          }
        })
        .build()

    fun newIntent(
      context: Context?,
      embeddedUrl: String?
    ): Intent {
      val intent: Intent = Intent(context, WebViewActivity::class.java)
      val bundle: Bundle = Bundle()
      bundle.putString("embedded_url", embeddedUrl)
      intent.putExtras(bundle)
      return intent
    }
  }
}