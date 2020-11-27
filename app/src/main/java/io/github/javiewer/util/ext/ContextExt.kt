package io.github.javiewer.util.ext

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
fun Context.isConnectedNetwork(): Boolean = run {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    activeNetwork?.isConnectedOrConnecting == true
}

inline fun <T> String.getEmptyOrDefault(default: () -> T): T {
    if (isNullOrEmpty() || this == "null") {
        return default()
    } else {
        return this as T
    }
}