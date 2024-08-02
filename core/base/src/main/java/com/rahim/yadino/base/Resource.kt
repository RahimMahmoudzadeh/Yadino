package com.rahim.yadino.base

import com.rahim.yadino.base.enums.error.ErrorMessageCode

sealed class Resource<T>(
    val data: T? = null,
    val message: ErrorMessageCode? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: ErrorMessageCode, data: T? = null) : Resource<T>(data, message)
}