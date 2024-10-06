package com.rahim.yadino

import com.rahim.yadino.enums.error.ErrorMessageCode

sealed class Resource<T>(
    val data: T? = null,
    val message: ErrorMessageCode? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: ErrorMessageCode?, data: T? = null) : Resource<T>(data, message)
}
