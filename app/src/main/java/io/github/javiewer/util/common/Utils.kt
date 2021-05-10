package io.github.javiewer.util.common

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 16/12/08
 * desc  : Utils初始化相关
</pre> *
 */
class Utils private constructor() {

  init {
    throw UnsupportedOperationException("u can't instantiate me...")
  }

  companion object {

    private var context: Context? = null

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    fun init(context: Context) {
      Companion.context = context.applicationContext
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    fun getContext(): Context {
      if (context != null) return context as Context
      throw NullPointerException("u should init first")
    }
  }

  fun capture(activity: Activity): Bitmap? {
    activity.window.decorView.isDrawingCacheEnabled = true
    return activity.window.decorView.drawingCache
  }

}