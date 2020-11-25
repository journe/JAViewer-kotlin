package io.github.javiewer.activity

import android.Manifest.permission
import android.R.string
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.orhanobut.logger.Logger
import io.github.javiewer.Configurations
import io.github.javiewer.JAViewer
import io.github.javiewer.Properties
import io.github.javiewer.R.layout
import io.github.javiewer.activity.MainActivity
import io.github.javiewer.adapter.item.DataSource
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

class StartActivity : AppCompatActivity() {

  private val viewModel: StartViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_start)
    checkPermissions() //检查权限，创建配置
  }

  private fun checkPermissions() {
    if (VERSION.SDK_INT >= VERSION_CODES.M && checkSelfPermission(
            permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
      Dexter.withActivity(this)
          .withPermission(permission.WRITE_EXTERNAL_STORAGE)
          .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
              checkPermissions()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
              AlertDialog.Builder(this@StartActivity)
                  .setTitle("权限申请")
                  .setCancelable(false)
                  .setMessage("JAViewer 需要储存空间权限，储存用户配置。请您允许。")
                  .setPositiveButton(
                      string.ok
                  ) { _, _ -> checkPermissions() }
                  .show()
            }

            override fun onPermissionRationaleShouldBeShown(
              permission: PermissionRequest,
              token: PermissionToken
            ) {
              token.continuePermissionRequest()
            }
          })
          .onSameThread()
          .check()
      return
    }
    val oldConfig: File = File(getExternalFilesDir(null), "configurations.json")
    val config: File = File(JAViewer.storageDir, "configurations.json")
    if (oldConfig.exists()) {
      oldConfig.renameTo(config)
    }
    val noMedia: File = File(JAViewer.storageDir, ".nomedia")
    try {
      noMedia.createNewFile()
    } catch (e: IOException) {
      e.printStackTrace()
    }
    JAViewer.CONFIGURATIONS = Configurations.load(config)
    readProperties()
  }

  private fun readProperties() {
    viewModel.tellme.observe(this) {
      Logger.d(it)
      val dataSources = listOf(DataSource("AVMOO 日本", it))
      JAViewer.DATA_SOURCES.addAll(dataSources)
      JAViewer.CONFIGURATIONS.dataSource = dataSources[0]
      JAViewer.CONFIGURATIONS.save()
      start()
    }

//    try {
//      assets.open("properties.json")
//          .use {
//            val properties: Properties = JAViewer.parseJson(
//                Properties::class.java, IOUtils.readText(it, IOUtils.UTF_8)
//            )
//            Logger.d(properties.dataSources.toString())
//            runOnUiThread { handleProperties(properties) }
//          }
//    } catch (ex: IOException) {
//      ex.printStackTrace()
//    }

//    val request: Request = Builder()
//        .url(
//            "https://raw.githubusercontent.com/ipcjs/JAViewer/master/app/src/main/assets/properties.json?t=" + System.currentTimeMillis() / 1000
//        )
//        .build()

//    JAViewer.HTTP_CLIENT.newCall(request)
//        .enqueue(object : Callback {
//          override fun onFailure(
//            call: Call,
//            e: IOException
//          ) {
//
//          }
//
//          @Throws(IOException::class)
//          override fun onResponse(
//            call: Call,
//            response: Response
//          ) {
//            val properties: Properties? = JAViewer.parseJson(
//                Properties::class.java, response.body()!!
//                .string()
//            )
//            if (properties != null) {
//              Handler(Looper.getMainLooper()).post { handleProperties(properties) }
//            }
//          }
//        })
  }

  fun handleProperties(properties: Properties) {
//    JAViewer.DATA_SOURCES.clear()
//    properties.dataSources?.let { JAViewer.DATA_SOURCES.addAll(it.toList()) }
//    JAViewer.hostReplacements.clear()
//    for (source: DataSource in JAViewer.DATA_SOURCES) {
//      try {
//        val host: String = URI(source.getLink()).host
//        for (h: String in source.legacies) {
//          JAViewer.hostReplacements[h] = host
//        }
//      } catch (e: URISyntaxException) {
//        e.printStackTrace()
//      }
//    }
//    val currentVersion: Int
//    try {
//      currentVersion = packageManager.getPackageInfo(packageName, 0).versionCode
//    } catch (e: NameNotFoundException) {
//      throw RuntimeException("Hacked???")
//    }
//    if (properties.latestVersionCode > 0 && currentVersion < properties.latestVersionCode) {
//      var message: String? = "新版本：" + properties.latestVersion
//      if (properties.changelog != null) {
//        message += "\n\n更新日志：\n\n" + properties.changelog + "\n"
//      }
//      val dialog: AlertDialog = AlertDialog.Builder(this)
//          .setTitle("发现更新")
//          .setMessage(message)
//          .setNegativeButton(
//              "忽略更新"
//          ) { _, _ -> start() }
//          .setPositiveButton("更新") { dialog, which ->
//            start()
//            startActivity(
//                Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://github.com/SplashCodes/JAViewer/releases")
//                )
//            )
//          }
//          .create()
//      dialog.show()
//    } else {
//      start()
//    }
  }

  fun start() {
    startActivity(Intent(this@StartActivity, MainActivity::class.java))
    finish()
  }
}