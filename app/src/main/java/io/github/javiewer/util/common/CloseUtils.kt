package io.github.javiewer.util.common

import java.io.Closeable
import java.io.IOException

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/10/9
 * desc  : 关闭相关工具类
</pre> *
 */
class CloseUtils private constructor() {
  companion object {
    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    @JvmStatic fun closeIO(vararg closeables: Closeable?) {
      if (closeables == null) return
      for (closeable in closeables) {
        if (closeable != null) {
          try {
            closeable.close()
          } catch (e: IOException) {
            e.printStackTrace()
          }
        }
      }
    }

    /**
     * 安静关闭IO
     *
     * @param closeables closeable
     */
    fun closeIOQuietly(vararg closeables: Closeable?) {
      if (closeables == null) return
      for (closeable in closeables) {
        if (closeable != null) {
          try {
            closeable.close()
          } catch (ignored: IOException) {
          }
        }
      }
    }
  }

  init {
    throw UnsupportedOperationException("u can't instantiate me...")
  }
}