package com.rahim.yadino.base

sealed class LoadableData<out T> {
  abstract val data: T?

  data object Initial : LoadableData<Nothing>() {
    override val data = null
  }

  data object Loading : LoadableData<Nothing>() {
    override val data = null
  }

  data class Loaded<T>(override val data: T) : LoadableData<T>()
}
