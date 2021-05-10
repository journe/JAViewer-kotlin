package io.github.javiewer.util.ext

import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import io.github.javiewer.BuildConfig
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchThrowException(block: suspend CoroutineScope.() -> Unit) {
    launch {
        try {
            block.invoke(this)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }
}

fun launchCoroutine(
    block: suspend (CoroutineScope) -> Unit,
    error: ((e: Exception) -> Unit)? = null,
    context: CoroutineContext = Dispatchers.Main
): Job {
    return GlobalScope.launch(context + CoroutineExceptionHandler { _, e ->
        Logger.e("==>coroutineException", e.message)    //1
    }) {
        try {
            block(this)
        } catch (e: Exception) {        //2
            Logger.e("==>coroutineError", e.message)
            if (error != null) {
                error(e)
            }
        }
    }
}

inline fun <reified E : Any> List<E>.toGson(): Type = object : TypeToken<List<E>>() {}.type