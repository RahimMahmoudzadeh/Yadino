package com.rahim.yadino.base

import com.rahim.yadino.enums.error.ErrorMessageCode

sealed class LoadableData<out T> {
    abstract val data: T?

    data object Initial : LoadableData<Nothing>() {
        override val data = null
    }

    data object Loading : LoadableData<Nothing>() {
        override val data = null
    }

    data class Loaded<T>(override val data: T) : LoadableData<T>()

    data class Error(val error: ErrorMessageCode) : LoadableData<Nothing>() {
        override val data = null
    }
}

val LoadableData<*>.isLoading: Boolean
    get() = this is LoadableData.Loading
