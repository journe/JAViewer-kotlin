package io.github.javiewer.model.repository

sealed class JavResult<out T> {
  data class Success<out T>(val value: T) : JavResult<T>()

  data class Failure(val throwable: Throwable?) : JavResult<Nothing>()
}

inline fun <reified T> JavResult<T>.doSuccess(success: (T) -> Unit) {
  if (this is JavResult.Success) {
    success(value)
  }
}

inline fun <reified T> JavResult<T>.doFailure(failure: (Throwable?) -> Unit) {
  if (this is JavResult.Failure) {
    failure(throwable)
  }
}
