package com.api.models.responses

import kotlinx.serialization.Serializable

sealed interface ApiResponse {
    @Serializable
    data class Data<T>(val data: T): ApiResponse

    @Serializable
    data class Error(val errors: List<ErrorDetails>): ApiResponse {
        constructor(title: String, code: Int): this(listOf(ErrorDetails(title, code)))

        @Serializable
        data class ErrorDetails(val title: String, val code: Int, val reason: String? = null)
    }
}