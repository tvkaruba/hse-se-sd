package com.api.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(val name: String, val email: String)