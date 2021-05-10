package io.github.javiewer.util.ext

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.view.ViewCompat
import io.github.javiewer.util.common.SizeUtils.Companion.dp2px
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by journey on 2019-12-26.
 */
/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
  this.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(editable: Editable?) {
      afterTextChanged.invoke(editable.toString())
    }

    override fun beforeTextChanged(
      s: CharSequence,
      start: Int,
      count: Int,
      after: Int
    ) {
    }

    override fun onTextChanged(
      s: CharSequence,
      start: Int,
      before: Int,
      count: Int
    ) {
    }
  })
}

class AutoDisposableJob(
  private val view: View,
  private val wrapped: Job
) : Job by wrapped, View.OnAttachStateChangeListener {

  init {
    if (ViewCompat.isAttachedToWindow(view)) {
      view.addOnAttachStateChangeListener(this)
    } else {
      cancel()
    }
    invokeOnCompletion {
      view.removeOnAttachStateChangeListener(this)
    }
  }

  override fun onViewDetachedFromWindow(v: View?) {
    cancel()
    view.removeOnAttachStateChangeListener(this)
  }

  override fun onViewAttachedToWindow(v: View?) = Unit

}

fun Job.autoDispose(view: View) =
  AutoDisposableJob(view, this)

fun View.onClick(
  context: CoroutineContext = Dispatchers.Main,
  handler: suspend CoroutineScope.(v: View?) -> Unit
) {
  setOnClickListener { v ->
    GlobalScope.launch(context, CoroutineStart.DEFAULT) {
      handler(v)
    }
        .autoDispose(v)
  }
}

/*** 可见性相关 ****/
fun View.gone() {
  visibility = View.GONE
}

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun View.popup() {
  animate().translationY(-0f)
}

fun View.animateY(y: Int) {
  animate().translationY(dp2px(y.toFloat()).toFloat())
}

val View.isGone: Boolean
  get() {
    return visibility == View.GONE
  }

val View.isVisible: Boolean
  get() {
    return visibility == View.VISIBLE
  }

val View.isInvisible: Boolean
  get() {
    return visibility == View.INVISIBLE
  }

/**
 * 切换View的可见性
 */
fun View.toggleVisibility() {
  visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
}
