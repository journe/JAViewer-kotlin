package io.github.javiewer

import android.content.Context

object AppHelper {
    lateinit var mContext: Context

    fun init(context: Context) {
        this.mContext = context
    }
}