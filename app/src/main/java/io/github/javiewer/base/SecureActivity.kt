package io.github.javiewer.base

import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity

open class SecureActivity constructor() : AppCompatActivity() {
  override fun onPause() {
    super.onPause()
    getWindow().setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE)
  }

  override fun onResume() {
    super.onResume()
    getWindow().clearFlags(LayoutParams.FLAG_SECURE)
  }
}