package com.example.weather.domain.utils

sealed class ResourceEither<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T?): ResourceEither<T>(data)

    class Error<T>(message: String, data: T? = null): ResourceEither<T>(data, message)
}